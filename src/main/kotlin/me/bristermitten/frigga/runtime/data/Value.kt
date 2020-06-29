package me.bristermitten.frigga.runtime.data

import me.bristermitten.frigga.runtime.type.*


data class Value(val type: Type, val value: Any)

fun intValue(value: Long) = Value(IntType, value)
fun decValue(value: Double) = Value(DecType, value)
fun stringValue(value: String) = Value(StringType, value)
fun charValue(value: Char) = Value(CharType, value)

//fun functionValue(function: CommandFunctionDefinition) = Value(
//    FunctionType(function.signature), FunctionValue(
//        function.signature,
//        function.body
//    )
//)
