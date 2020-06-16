package me.bristermitten.frigga.runtime.command.function

import me.bristermitten.frigga.ast.element.function.Signature
import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.functionValue

data class CommandFunctionDefinition(
    val signature: Signature,
    val body: List<Command>
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        stack.push(functionValue(this))
    }
}
