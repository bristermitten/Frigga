package me.bristermitten.frigga.runtime.command.operator

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.Value
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.error.NoSuchFunctionException

data class CommandBinaryAdd(val left: Command, val right: Command) : Command() {
    override fun eval(stack: Stack, context: FriggaContext) {
        left.eval(stack, context)
        val leftValue = stack.pull() as Value

        right.eval(stack, context)
        val rightValue = stack.pull() as Value

        val addFunctions = leftValue.type.getFunctions(OPERATOR_ADD_NAME)
        val addFunction = //TODO some sort of type prioritisation
            addFunctions.minBy {
                val parameterTypes = it.signature.input.values
                if (parameterTypes.size != 1) -1
                else parameterTypes.first().distanceTo(rightValue.type)
            } ?: throw NoSuchFunctionException(leftValue.type, OPERATOR_ADD_NAME, listOf(rightValue.type))

        stack.push(leftValue)
        addFunction.call(OPERATOR_ADD_NAME, leftValue, stack, context, listOf(rightValue))

        val result = stack.pull() as Value
        stack.push(result)

    }
}

data class CommandBinarySubtract(val left: Command, val right: Command) : Command() {
    override fun eval(stack: Stack, context: FriggaContext) {
        left.eval(stack, context)
        val leftValue = stack.pull() as Value

        right.eval(stack, context)
        val rightValue = stack.pull() as Value

        val type = leftValue.type union rightValue.type

        val result: Number =
            when {
                leftValue.value is Number && rightValue.value is Number -> {
                    when {
                        leftValue.value is Long && rightValue.value is Long -> leftValue.value - rightValue.value
                        leftValue.value is Double || rightValue.value is Double -> leftValue.value.toDouble() - rightValue.value.toDouble()
                        else -> throw IllegalArgumentException("$leftValue - $rightValue")
                    }
                }
                else -> throw IllegalArgumentException("$leftValue - $rightValue")
            }
        stack.push(
            Value(type, result)
        )
    }
}
