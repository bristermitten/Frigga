package me.bristermitten.frigga.runtime.type

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.data.*
import me.bristermitten.frigga.runtime.data.Function
import me.bristermitten.frigga.util.set
import kotlin.collections.set

open class Type(
    val name: String,
    private val parent: Type? = AnyType
) {
    init {
        @Suppress("LeakingThis")
        types[name] = this
    }

    val staticProperty = Property(
        name,
        emptySet(),
        Value(this, Unit)
    )

    private val typeFunctions: Multimap<String, Function> = HashMultimap.create()
    fun getFunctions(name: String): Collection<Function> = typeFunctions[name]

    open infix fun union(other: Type): Type =
        AnyType

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

    protected open fun coerceValueTo(value: Value, other: Type): Value =
        Value(other, value.value)


    protected fun defineFunction(function: Function) {
        this.typeFunctions[function.name] = function
    }
}

object NumType : Type(
    "Num",
    AnyType
)

object IntType : Type(
    "Int",
    NumType
) {
    override fun union(other: Type): Type {
        return when (other) {
            is DecType -> DecType
            is IntType -> IntType
            else -> super.union(other)
        }
    }

    override fun coerceValueTo(value: Value, other: Type): Value {
        return when (other) {
            is DecType -> Value(
                other,
                (value.value as Long).toDouble()
            )
            else -> super.coerceValueTo(value, other)
        }
    }

}

object DecType : Type(
    "Dec",
    NumType
) {
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

object StringType : Type(
    "String",
    AnyType
)

object CharType : Type(
    "CharType",
    AnyType
)

object AnyType : Type("Any", null) {
    override fun accepts(other: Type) = true
}

object NothingType : Type("Nothing", null) {
    override fun isSubtypeOf(other: Type): Boolean {
        return true
    }
}

internal class SimpleType(name: String) : Type(name)

data class JVMType(val jvmClass: Class<*>) : Type(jvmClass.simpleName) {

    override fun accepts(other: Type): Boolean {
        return true //JVM type will accept any value, actual validation is done by the JVM via reflection
    }
}

object CallerType : Type("__Caller")


object OutputType : Type("Output") {
    init {
        defineFunction(
            Function(
                "println",
                Signature(
                    emptyMap(), mapOf("value" to AnyType), NothingType
                ),
                listOf(
                    CommandNode(object : Command() {
                        override fun eval(stack: Stack, context: FriggaContext) {
                            println(context.findProperty("value")?.value?.value)
                        }

                    }, Position(0, 0))
                )
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

fun getJVMType(jvmClass: Class<*>) = types.getOrPut(jvmClass.simpleName) {
    JVMType(jvmClass)
}

val JVM_EQUIVALENTS = mapOf(
    getJVMType(Int::class.java) to IntType,
    getJVMType(Long::class.java) to IntType,
    getJVMType(Double::class.java) to DecType,
    getJVMType(Float::class.java) to DecType
)

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
