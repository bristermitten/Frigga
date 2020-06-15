package me.bristermitten.frigga.ast.element.expression.value

import me.bristermitten.frigga.ast.element.expression.Expression

data class ReferencedCall(
    val calling: String,
    val args: List<Expression>
) : Expression
