package me.bristermitten.frigga.ast.element.exp.value

import me.bristermitten.frigga.ast.element.exp.Expression

data class Call(
    val calling: String,
    val args: List<Expression>
) : Expression
