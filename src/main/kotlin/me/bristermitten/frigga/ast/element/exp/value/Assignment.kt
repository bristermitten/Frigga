package me.bristermitten.frigga.ast.element.exp.value

import me.bristermitten.frigga.ast.element.exp.Expression

data class Assignment(
    val assignTo: String,
    val value: Expression
) : Expression
