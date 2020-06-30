package me.bristermitten.frigga.runtime.data

import CharType
import StringType
import me.bristermitten.frigga.runtime.type.*


data class Value(val type: Type, val value: Any)

fun intValue(value: Long) = Value(IntType, value)
fun decValue(value: Double) = Value(DecType, value)
fun stringValue(value: String) = Value(StringType, value)
fun charValue(value: Char) = Value(CharType, value)
