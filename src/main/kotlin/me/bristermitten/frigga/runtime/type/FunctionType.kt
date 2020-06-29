package me.bristermitten.frigga.runtime.type

import me.bristermitten.frigga.runtime.data.Signature

class FunctionType(
    signature: Signature
) : Type(
    signature.params.values.joinToString(prefix = "(", postfix = ")") { it.name } + " -> " + signature.returned.name
)
