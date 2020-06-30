package me.bristermitten.frigga.runtime.type

import me.bristermitten.frigga.runtime.data.Value

data class TypeInstance(
    val type: Type,
    val properties: Map<TypeProperty, Value>
)
