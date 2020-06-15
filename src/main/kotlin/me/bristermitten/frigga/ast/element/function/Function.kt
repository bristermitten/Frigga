package me.bristermitten.frigga.ast.element.function

import me.bristermitten.frigga.ast.element.Named
import me.bristermitten.frigga.ast.element.expression.Expression

internal data class Function(
    override val name: String,
    val signature: Signature,
    val contents: List<Any>
) : Named, Expression
