package me.bristermitten.frigga.ast.element.expression.value

import me.bristermitten.frigga.ast.element.Type
import me.bristermitten.frigga.ast.element.expression.Expression

internal data class Lambda(
    val params: Map<String, LambdaParam>,
    val body: List<Expression>
) : Expression {
    fun toCodeString(): String {
        return buildString {

            append('(')

            params.values.forEach {
                append(it.name)
                if (it.type != null) {
                    append("::")
                    append(it.type.name)
                }
                append(',')
                append(' ')
            }

            trim()
            append(')')

            append(" -> {\n")
            body.forEach {
                append(it)
            }
            append('}')
        }
    }
}

internal data class LambdaParam(
    val name: String,
    val type: Type? = null
)
