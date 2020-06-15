package me.bristermitten.frigga.ast.element.expression.value

import me.bristermitten.frigga.ast.element.expression.Expression

data class VarReference(
    val referencing: String
) : Expression {
    override fun toString() = referencing
}
