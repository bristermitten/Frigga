package me.bristermitten.frigga.ast.element.expression.value

import me.bristermitten.frigga.ast.element.expression.Expression

class BinaryOperator(
    val left: Expression,
    val operator: String,
val right: Expression
) : Expression{
}
