package me.bristermitten.frigga.ast.element

import me.bristermitten.frigga.runtime.Value

internal data class Property(
    override val name: String,
    val modifiers: Set<Modifier>,
    var value: Value
) : Named
