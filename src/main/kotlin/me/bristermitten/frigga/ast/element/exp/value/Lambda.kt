package me.bristermitten.frigga.ast.element.exp.value

import me.bristermitten.frigga.ast.element.Type
import me.bristermitten.frigga.ast.element.exp.Expression

internal data class Lambda(
    val params: Map<String, LambdaParam>,
    val body: List<Expression>
) : Expression

internal data class LambdaParam(
    val name: String,
    val type: Type? = null
)
