package me.bristermitten.frigga.ast.element

internal data class Property(
    override val name: String,
    val type: Type,
    val modifiers: Set<Modifier>,
    var value: Any
) : Named
