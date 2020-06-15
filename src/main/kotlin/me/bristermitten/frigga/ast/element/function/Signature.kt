package me.bristermitten.frigga.ast.element.function

import me.bristermitten.frigga.ast.element.Type

internal data class Signature(
    val typeSignature: Map<String, Type>,
    val input: Map<String, Type>,
    val output: Type
)
