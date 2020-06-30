package me.bristermitten.frigga.runtime.type

data class TypeProperty(
    val name: String,
    val type: Type,
    val value: Any?
)
