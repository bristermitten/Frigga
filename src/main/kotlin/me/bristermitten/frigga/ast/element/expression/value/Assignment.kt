package me.bristermitten.frigga.ast.element.expression.value

import me.bristermitten.frigga.ast.element.expression.Expression

data class Assignment(
    val assignTo: String,
    val value: Expression
) : Expression {
    override fun toString(): String {
        return buildString {
            append(assignTo)
                .append(" = ")
                .append(value)
        }
    }
}
