package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.ast.element.DecType
import me.bristermitten.frigga.ast.element.IntType
import me.bristermitten.frigga.ast.element.Type

data class Value(val type: Type, val value: Any)

fun intValue(value: Long) = Value(IntType, value)
fun decValue(value: Double) = Value(DecType, value)
