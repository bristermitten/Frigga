package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.CommandNode

data class CommandParenthesisExpression(
    val command: CommandNode
) : Command() {
    override fun eval(stack: Stack, context: FriggaContext) {
        command.command.eval(stack, context)
    }
}
