package me.bristermitten.frigga.ast.element.expression.value

import me.bristermitten.frigga.ast.element.expression.Expression

data class Call(
    val upon: Expression?,
    val calling: String,
    val args: List<Expression>
) : Expression {
    override fun toString(): String {
        return buildString {
            append(calling)
            append('(')
            append(args.joinToString(",") {
                it.toString()
            })
            append(')')
        }
    }
}
