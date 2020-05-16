package me.bristermitten.frigga.rewrite.function

import me.bristermitten.frigga.rewrite.identifier.Identifier
import me.bristermitten.frigga.rewrite.type.Type

data class FunctionSignature(
    val params: List<FunctionParameter>,
    val returned: Type
)

data class FunctionParameter(
    val name: Identifier,
    val type: Type
)
