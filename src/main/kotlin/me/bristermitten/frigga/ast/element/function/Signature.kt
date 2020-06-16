package me.bristermitten.frigga.ast.element.function

import me.bristermitten.frigga.ast.element.NothingType
import me.bristermitten.frigga.ast.element.Type

data class Signature(
    val typeSignature: Map<String, Type> = emptyMap(),
    val input: Map<String, Type> = emptyMap(),
    val output: Type = NothingType
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
