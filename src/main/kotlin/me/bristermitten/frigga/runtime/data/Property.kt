package me.bristermitten.frigga.runtime.data

data class Property(
    val name: String,
    val modifiers: Set<Modifier>,
    var value: Value
)
