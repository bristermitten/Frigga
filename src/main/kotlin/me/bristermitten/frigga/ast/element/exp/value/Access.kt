package me.bristermitten.frigga.ast.element.exp.value

import me.bristermitten.frigga.ast.element.exp.Expression

data class Access(
    val upon: Expression,
    val property: Expression
) : Expression
