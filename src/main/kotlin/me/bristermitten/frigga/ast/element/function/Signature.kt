package me.bristermitten.frigga.ast.element.function

import me.bristermitten.frigga.ast.element.Type

data class Signature(
    val typeSignature: Map<String, Type>,
    val input: Map<String, Type>,
    val output: Type
) {
    override fun toString(): String {
        return buildString {
            if (typeSignature.isNotEmpty()) {
                append('<')
                typeSignature.forEach {
                    append(it.key)
                    append("::")
                    append(it.value)
                    if (typeSignature.size > 1) {
                        append(", ")
                    }
                }
                trim()
                append('>')
            }

            append('(')
            input.forEach {
                append(it.key)
                append("::")
                append(it.value.name)
                if(input.size > 1 ){
                    append(',')
                }
                append(' ')
            }
            trim()
            append(')')

            append(" -> ")
            append(output.name)

        }
    }
}
