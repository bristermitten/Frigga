package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.ast.element.DecType
import me.bristermitten.frigga.ast.element.FunctionType
import me.bristermitten.frigga.ast.element.IntType
import me.bristermitten.frigga.ast.element.Type
import me.bristermitten.frigga.runtime.command.function.CommandFunctionDefinition
import me.bristermitten.frigga.runtime.command.function.FunctionValue

data class Value(val type: Type, val value: Any)

fun intValue(value: Long) = Value(IntType, value)
fun decValue(value: Double) = Value(DecType, value)

fun functionValue(function: CommandFunctionDefinition) = Value(
    FunctionType(function.signature), FunctionValue(
        function.signature,
        function.body
    )
)
