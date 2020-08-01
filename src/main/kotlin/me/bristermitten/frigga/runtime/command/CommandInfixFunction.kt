package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.CommandNode

data class CommandInfixFunction(
    val left: CommandNode,
    val right: CommandNode,
    val functionName: String
) : Command()
{

    private val command = CommandCall(left, functionName, listOf(right))
    override fun eval(stack: Stack, context: FriggaContext)
    {
        command.eval(stack, context)
    }

}
