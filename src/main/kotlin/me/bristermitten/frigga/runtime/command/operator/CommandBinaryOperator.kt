package me.bristermitten.frigga.runtime.command.operator

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.Value
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.error.NoSuchFunctionException

data class CommandBinaryOperator(val left: Command, val right: Command, val operator: String) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        left.eval(stack, context)
        val leftValue = stack.pull() as Value

        right.eval(stack, context)
        val rightValue = stack.pull() as Value

        val operatorFunctions = leftValue.type.getFunctions(operator)

        val operatorFun = operatorFunctions.minBy {
                val parameterTypes = it.signature.input.values
                if (parameterTypes.size != 1) -1
                else parameterTypes.first().distanceTo(rightValue.type)
            } ?: throw NoSuchFunctionException(leftValue.type, operator, listOf(rightValue.type))

        stack.push(leftValue)

        operatorFun.call(operator, leftValue, stack, context, listOf(rightValue))

        val result = stack.pull() as Value
        stack.push(result)

    }
}
