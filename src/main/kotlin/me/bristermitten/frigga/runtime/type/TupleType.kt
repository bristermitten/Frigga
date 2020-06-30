package me.bristermitten.frigga.runtime.type

data class TupleType(
    val types: Map<String, Type>
) : Type(types.values.joinToString(prefix = "(", postfix = ")"))
