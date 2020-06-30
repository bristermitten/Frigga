package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.UPON_NAME
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.type.TypeInstance

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

        val typeInstance = leftValue.value as? TypeInstance
        val functionName = if (typeInstance == null) {
            context.findFunction(leftValue.type, function, listOf(rightValue.type))
        } else {
            context.findTypeFunction(leftValue.type, typeInstance, function, listOf(rightValue.type))
        }

        requireNotNull(functionName) {
            "No such function ${leftValue.type}#$function"
        }

        context.defineProperty(UPON_NAME, leftValue, true)

        functionName.call(stack, context, listOf(rightValue))

        val result = stack.pull()

        stack.push(result)
    }

}
