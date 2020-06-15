package me.bristermitten.frigga.ast.element

import me.bristermitten.frigga.ast.element.expression.Expression

internal data class FriggaFile(
    override val name: String,
    val contents: List<Expression>,
    val using: Set<Namespace>
) : Named {
    override fun toString(): String {
        return buildString {
            using.forEach {
                append("use ").append('"').append(it.name).append('"').append('\n')
            }
            contents.forEach {
                append(it.toString())
            }
        }
    }
}
