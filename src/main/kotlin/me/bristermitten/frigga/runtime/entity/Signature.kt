package me.bristermitten.frigga.runtime.entity

import me.bristermitten.frigga.runtime.entity.type.Type

data class Signature(
    val returnType: Type,
    val params: Map<String, Type>
) : Type() {
    override val name: String = "(${params.values.joinToString(",")}) -> $returnType"
}
