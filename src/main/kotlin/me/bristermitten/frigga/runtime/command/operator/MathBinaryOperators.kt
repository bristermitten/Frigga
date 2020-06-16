package me.bristermitten.frigga.runtime.command.operator

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.Value
data class CommandBinaryAdd(val left: Command, val right: Command) : Command() {
    override fun eval(stack: Stack, context: FriggaContext) {
        left.eval(stack, context)
        val leftValue = stack.pull() as Value

        right.eval(stack, context)
        val rightValue = stack.pull() as Value

        val type = leftValue.type union rightValue.type
        val result =
            when {
                leftValue.value is Number && rightValue.value is Number -> {
                    when {
                        leftValue.value is Long && rightValue.value is Long -> leftValue.value + rightValue.value
                        leftValue.value is Double || rightValue.value is Double -> leftValue.value.toDouble() + rightValue.value.toDouble()
                        else -> throw IllegalArgumentException("$leftValue + $rightValue")
                    }
                }
                else -> throw IllegalArgumentException("$leftValue + $rightValue")
            }
        stack.push(
            Value(
                type,
                result
            )
        )
    }
}

data class CommandBinarySubtract(val left: Command, val right: Command) : Command() {
    override fun eval(stack: Stack, context: FriggaContext) {
        left.eval(stack, context)
        val leftValue = stack.pull() as Value

        right.eval(stack, context)
        val rightValue = stack.pull() as Value

        val type = leftValue.type union rightValue.type

        val result =
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
