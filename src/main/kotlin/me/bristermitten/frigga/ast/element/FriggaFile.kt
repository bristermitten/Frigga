package me.bristermitten.frigga.ast.element

import me.bristermitten.frigga.ast.element.exp.Expression

internal data class FriggaFile(
    override val name: String,
    val contents: List<Expression>,
    val using: Set<Namespace>
) : Named
