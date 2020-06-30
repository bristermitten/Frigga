package me.bristermitten.frigga.runtime.data

import me.bristermitten.frigga.runtime.type.Type

data class PropertyDeclaration(
    val modifiers: Set<Modifier> = emptySet(),
    val name: String,
    val type: Type
)
