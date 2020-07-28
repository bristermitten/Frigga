package me.bristermitten.frigga.runtime.type

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.data.Property
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.data.function.*
import me.bristermitten.frigga.runtime.data.function.Function
import me.bristermitten.frigga.util.set

abstract class Type(
    val name: String,
    val parent: Type? = AnyType
) {
    val staticProperty = Property(
        name,
        emptySet(),
        Value(this, Unit)
    )
    private val properties: Multimap<String, TypeProperty> = HashMultimap.create()

    fun getProperty(name: String): TypeProperty? {
        return properties[name].firstOrNull { it.type !is FunctionType }
    }

    fun getFunctions(name: String): List<TypeProperty> {
        return properties[name].filter { it.type is FunctionType } + (parent?.getFunctions(name) ?: emptyList())
    }

    fun getFunction(name: String, params: List<Type>): TypeProperty? {
        val byName = getFunctions(name)
            .filter {
                (it.type as FunctionType).signature.typesMatch(params)
            }

        return byName.minBy { func ->
            val itParams = (func.type as FunctionType).signature.params.values.toList()
            val paramDistance = itParams.withIndex().sumBy { it.value.distanceTo(params[it.index]) }
            paramDistance
        }
    }

    protected fun defineProperty(property: TypeProperty) {
        val existing = properties[property.name]
        require(existing.isEmpty()) {
            "Cannot redefine existing Type Property ${property.name}"
        }
        properties[property.name] = property
    }

    protected fun defineFunction(function: Function) {
        properties[function.name] = TypeProperty(function.name, FunctionType(function.signature), function)
    }

    protected inline fun defineFunction(closure: FunctionDefiner.() -> Unit) {
        val value = function(closure)
        defineFunction(value)
    }

    open fun isSubtypeOf(other: Type): Boolean {
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
        if (value.type.isSubtypeOf(other)) {
            return value //avoid unnecessary coercion between things like Int and Any
        }
        require(other.accepts(value.type)) {
            "Cannot coerce between incompatible types ${value.type} and $other"
        }

        if (other is FunctionType && !this.accepts(other)) { //No coercion necessary if the 2 function types are the same
            if (other.signature.params.isEmpty() && other.signature.returned.accepts(this)) {
                return Value(other, function {
                    signature {
                        output = this@Type
                    }
                    body { stack, _ ->
                        stack.push(value)
                    }
                }) //allow coercion between things like Int and () -> Int
            }
        }

        return coerceValueTo(value, other)
    }

    open fun accepts(other: Type): Boolean {
        if (this is FunctionType) {
            if (signature.params.isEmpty() && signature.returned.accepts(other)) {
                return true //allow coercion between things like Int and () -> Int
            }
        }
        val relationship = relationshipTo(other)
        return relationship == TypeRelationship.Same || relationship == TypeRelationship.Subtype
    }

    protected open fun coerceValueTo(value: Value, other: Type) = Value(other, value.value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Type) return false

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    open fun reestablish(context: FriggaContext): Type = this

}
