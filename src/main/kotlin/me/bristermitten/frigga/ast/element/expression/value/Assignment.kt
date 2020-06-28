package me.bristermitten.frigga.ast.element.expression.value

import me.bristermitten.frigga.ast.element.Modifier
import me.bristermitten.frigga.ast.element.expression.Expression

data class Assignment(
    val assignTo: String,
    val modifiers: Set<Modifier>,
    val value: Expression
) : Expression
