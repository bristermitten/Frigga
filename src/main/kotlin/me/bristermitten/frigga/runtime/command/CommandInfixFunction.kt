package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.data.function.Signature
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.type.AnyType

class CommandInfixFunction(
    val left: CommandNode,
    val right: CommandNode,
    val function: String
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        left.command.eval(stack, context)
        val leftValue = stack.pull()

        right.command.eval(stack, context)
        val rightValue = stack.pull()

        val functionName = leftValue.type.getFunction(
            function, Signature(
                emptyMap(),
                mapOf("value" to rightValue.type),
                AnyType
            )
        )

        stack.push(leftValue)

        functionName.call(stack, context, listOf(rightValue))

        val result = stack.pull() as Value
        stack.push(result)
    }

}
