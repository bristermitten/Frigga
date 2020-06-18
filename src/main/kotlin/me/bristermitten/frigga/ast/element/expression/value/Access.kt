package me.bristermitten.frigga.ast.element.expression.value

import me.bristermitten.frigga.ast.element.expression.Expression

data class Access(
    val upon: Expression,
    val property: String
) : Expression
