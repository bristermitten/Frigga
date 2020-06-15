package me.bristermitten.frigga.ast.element.exp.value

import me.bristermitten.frigga.ast.element.exp.Expression

class BinaryOperator(
    val left: Expression,
    val operator: String,
val right: Expression
) : Expression{
}
