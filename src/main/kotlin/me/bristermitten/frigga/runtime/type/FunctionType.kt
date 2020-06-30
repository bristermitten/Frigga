package me.bristermitten.frigga.runtime.type

import me.bristermitten.frigga.runtime.data.function.Signature

data class FunctionType(
    val signature: Signature
) : Type(
    signature.params.values.joinToString(prefix = "(", postfix = ")") { it.name } + " -> " + signature.returned.name
) {
    override fun accepts(other: Type): Boolean {
        if (other is FunctionType) {
            return signature.matches(other.signature)
        }

        return super.accepts(other)
    }

    override fun toString() = name

}
