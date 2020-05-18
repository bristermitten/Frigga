package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.runtime.expression.Expression

data class FriggaFile(
    val contents: List<Expression>
) {
    override fun toString(): String {
        return buildString {
            append("FriggaFile {\n")
            contents.forEach {
                append("  ")
                append(it)
                append('\n')
            }
            append("}")
        }

    }
}
