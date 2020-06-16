package me.bristermitten.frigga.ast.element

import me.bristermitten.frigga.ast.element.function.Signature
import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.function.FunctionValue

sealed class Type(
    override val name: String,
    val parent: Type? = AnyType
) : Named {

    init {
        _types[name] = this
    }

    protected val typeFunctions: MutableMap<String, FunctionValue> = mutableMapOf()
    val functions: Map<String, FunctionValue> = typeFunctions

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
}

object StringType : Type("String", AnyType)

object AnyType : Type("Any", null)
object NothingType : Type("Nothing", null) {
    override fun isSubtypeOf(other: Type): Boolean {
        return true
    }
}

internal data class SimpleType(override val name: String) : Type(name)
internal data class FunctionType(val signature: Signature) : Type(signature.toString())

data class JVMType(val jvmClass: Class<*>) : Type(jvmClass.simpleName) {
    init {
        jvmClass.methods.forEach {
            typeFunctions[it.name] = FunctionValue(
                Signature(
                    emptyMap(),
                    it.parameters.map { param ->
                        param.name to (_types[name] ?: JVMType(param.type))
                    }.toMap(),
                    (_types[it.returnType.simpleName] ?: JVMType(it.returnType))
                ),
                listOf(object : Command() {
                    override fun eval(stack: Stack, context: FriggaContext) {
                        val upon = context.findProperty("__upon")!!.value
                        it.invoke(upon.value, *it.parameters.map {
                            val findProperty = context.findProperty(it.name)
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

object BreakException : Exception()

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

private val _types = mutableMapOf<String, Type>()

fun loadTypes() {
    fun Type.load() = _types.put(name, this)
    NumType.load()
    IntType.load()
    DecType.load()
    StringType.load()
    AnyType.load()
    NothingType.load()
    CallerType.load()
    OutputType.load()
}

fun getType(name: String) = _types.getOrPut(name) {
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
