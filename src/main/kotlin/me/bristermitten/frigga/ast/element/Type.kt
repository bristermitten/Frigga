package me.bristermitten.frigga.ast.element

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import me.bristermitten.frigga.ast.element.function.*
import me.bristermitten.frigga.runtime.*
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.function.FunctionValue
import me.bristermitten.frigga.runtime.command.operator.OPERATOR_ADD_NAME
import me.bristermitten.frigga.runtime.command.operator.OPERATOR_DIVIDE_NAME
import me.bristermitten.frigga.runtime.command.operator.OPERATOR_TAKE_NAME
import me.bristermitten.frigga.runtime.command.operator.OPERATOR_TIMES_NAME
import me.bristermitten.frigga.runtime.error.BreakException
import me.bristermitten.frigga.util.set

sealed class Type(
    final override val name: String,
    private val parent: Type? = AnyType
) : Named {
    init {
        @Suppress("LeakingThis")
        types[name] = this
    }

    protected val typeFunctions: Multimap<String, FunctionValue> = HashMultimap.create()
    fun getFunctions(name: String): Collection<FunctionValue> = typeFunctions[name]

    open infix fun union(other: Type): Type = AnyType

    infix fun relationshipTo(other: Type): TypeRelationship {
        if (this === other) return TypeRelationship.Same
        if (this.parent === other.parent) return TypeRelationship.Sibling
        val isSubtype = isSubtypeOf(other)
        if (isSubtype) {
            return TypeRelationship.Subtype
        }
        val isSupertype = other.isSubtypeOf(this)
        if (isSupertype) {
            return TypeRelationship.Supertype
        }
        return TypeRelationship.NoRelationship
    }

    fun distanceTo(other: Type): Int {
        return when {
            this === other -> 0
            this.isSubtypeOf(other) || other.isSubtypeOf(this) -> 1
            else -> Int.MAX_VALUE
        }
    }

    open fun accepts(other: Type): Boolean {
        val relationshipTo = relationshipTo(other)
        return relationshipTo == TypeRelationship.Same || relationshipTo == TypeRelationship.Supertype
    }

    protected open fun isSubtypeOf(other: Type): Boolean {
        var thisParent = this.parent
        while (thisParent != null) {
            if (thisParent === other) {
                return true
            }
            thisParent = thisParent.parent
        }
        return false
    }

    fun coerceTo(value: Value, other: Type): Value {
        require(other.accepts(value.type)) {
            "Cannot coerce between incompatible types ${value.type} and $other"
        }
        return coerceValueTo(value, other)
    }

    protected open fun coerceValueTo(value: Value, other: Type): Value = Value(other, value.value)

    protected fun defineFunction(value: NamedFunctionValue) {
        typeFunctions[value.name] = value.functionValue
    }

    protected inline fun defineFunction(closure: FunctionDefiner.() -> Unit) {
        val value = function(closure)
        defineFunction(value)
    }
}

object NumType : Type("Num", AnyType)

object IntType : Type("Int", NumType) {
    override fun union(other: Type): Type {
        return when (other) {
            is DecType -> DecType
            is IntType -> IntType
            else -> super.union(other)
        }
    }

    override fun coerceValueTo(value: Value, other: Type): Value {
        return when (other) {
            is DecType -> Value(other, (value.value as Long).toDouble())
            else -> super.coerceValueTo(value, other)
        }
    }

    init {
        fun defineIntAndDecMathFunctions(
            name: String,
            intOperator: (Long, Long) -> Long,
            decOperator: (Long, Double) -> Double
        ) {
            defineFunction {
                this.name = name
                signature {
                    input = mapOf("value" to IntType)
                    output = IntType
                }
                body { stack, context ->
                    val thisValue = stack.pull() as Value
                    val addTo = context.findProperty("value")!!.value
                    stack.push(intValue(intOperator(thisValue.value as Long, addTo.value as Long)))
                }
            }
            defineFunction {
                this.name = name
                signature {
                    input = mapOf("value" to DecType)
                    output = DecType
                }
                body { stack, context ->
                    val thisValue = stack.pull() as Value
                    val addTo = context.findProperty("value")!!.value
                    stack.push(decValue(decOperator(thisValue.value as Long, addTo.value as Double)))
                }
            }
        }

        defineIntAndDecMathFunctions(OPERATOR_ADD_NAME, Long::plus, Long::plus)
        defineIntAndDecMathFunctions(OPERATOR_TAKE_NAME, Long::minus, Long::minus)
        defineIntAndDecMathFunctions(OPERATOR_TIMES_NAME, Long::times, Long::times)
        defineIntAndDecMathFunctions(OPERATOR_DIVIDE_NAME, Long::div, Long::div)
    }
}

object DecType : Type("Dec", NumType) {
    override fun union(other: Type): Type {
        return when (other) {
            is IntType, is DecType -> DecType
            else -> super.union(other)
        }
    }

    override fun accepts(other: Type): Boolean {
        return other == IntType || super.accepts(other)
    }

    init {
        fun defineMathFunction(
            name: String,
            decOperator: (Double, Double) -> Double
        ) {
            defineFunction {
                this.name = name
                signature {
                    input = mapOf("value" to DecType)
                    output = DecType
                }
                body { stack, context ->
                    val thisValue = stack.pull() as Value
                    val addTo = context.findProperty("value")!!.value
                    val addToAsDec = addTo.type.coerceTo(addTo, DecType)
                    stack.push(decValue(decOperator(thisValue.value as Double, addToAsDec.value as Double)))
                }
            }
        }
        defineMathFunction(OPERATOR_ADD_NAME, Double::plus)
        defineMathFunction(OPERATOR_TAKE_NAME, Double::minus)
        defineMathFunction(OPERATOR_TIMES_NAME, Double::times)
        defineMathFunction(OPERATOR_DIVIDE_NAME, Double::div)
    }

}

object StringType : Type("String", AnyType)

object AnyType : Type("Any", null)
object NothingType : Type("Nothing", null) {
    override fun isSubtypeOf(other: Type): Boolean {
        return true
    }
}

internal class SimpleType(name: String) : Type(name)
internal data class FunctionType(val signature: Signature) : Type(signature.toString())

data class JVMType(val jvmClass: Class<*>) : Type(jvmClass.simpleName) {
    init {
        jvmClass.methods.forEach {
            typeFunctions[it.name] = FunctionValue(
                Signature(
                    emptyMap(),
                    it.parameters.map { param ->
                        param.name to (types[name] ?: JVMType(param.type))
                    }.toMap(),
                    (types[it.returnType.simpleName] ?: JVMType(it.returnType))
                ),
                listOf(object : Command() {
                    override fun eval(stack: Stack, context: FriggaContext) {
                        val upon = context.findProperty(UPON_NAME)!!.value
                        it.invoke(upon.value, *it.parameters.map { param ->
                            val findProperty = context.findProperty(param.name)
                            findProperty?.value
                        }.toTypedArray())
                    }
                })
            )
        }
    }

    override fun accepts(other: Type): Boolean {
        return true //JVM type will accept any value, actual validation is done by the JVM via reflection
    }
}

object CallerType : Type("__Caller") {
    init {
        typeFunctions["break"] = FunctionValue(
            Signature(emptyMap(), emptyMap(), NothingType),
            listOf(
                object : Command() {
                    override fun eval(stack: Stack, context: FriggaContext) {
                        throw BreakException
                    }
                }
            )
        )
    }
}


object OutputType : Type("Output") {
    init {
        typeFunctions["println"] = FunctionValue(
            Signature(emptyMap(), mapOf("value" to AnyType), NothingType),
            listOf(
                object : Command() {
                    override fun eval(stack: Stack, context: FriggaContext) {
                        val toPrint = context.findProperty("value")
                        require(toPrint != null) {
                            "Nothing provided for \"value\""
                        }
                        println(toPrint.value.value)
                    }
                }
            )
        )
    }
}

private val types = mutableMapOf<String, Type>()

fun loadTypes() {
    fun Type.load() = types.put(name, this)
    NumType.load()
    IntType.load()
    DecType.load()
    StringType.load()
    AnyType.load()
    NothingType.load()
    CallerType.load()
    OutputType.load()
}

fun getType(name: String) = types.getOrPut(name) {
    SimpleType(name)
}

enum class TypeRelationship {
    Same,
    Supertype,
    Subtype,

    /**
     * The 2 types share a parent.
     */
    Sibling,

    /**
     * The 2 types have no link (except AnyType)
     */
    NoRelationship
}
