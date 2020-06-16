package me.bristermitten.frigga.runtime.command.function

import me.bristermitten.frigga.ast.element.function.Signature
import me.bristermitten.frigga.runtime.command.Command

data class FunctionValue(
    val signature: Signature,
    val body: List<Command>
)
