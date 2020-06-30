package me.bristermitten.frigga.runtime.data.structure

import me.bristermitten.frigga.runtime.data.Value

class StructValue(
    val type: Struct,
    val values: Map<String, Value>
)
