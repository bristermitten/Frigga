package me.bristermitten.frigga.runtime.type

import me.bristermitten.frigga.runtime.data.Value

data class TypeInstance(
    val type: Type,
    val properties: MutableMap<TypeProperty, Value>
) {
    override fun toString(): String
    {
        return "${type.name}{${properties.entries.joinToString { 
            it.key.property.name + " = " + it.value.value
        }}}"
    }
}
