package me.bristermitten.frigga.runtime.expression.binary

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.entity.type.BoolType
import me.bristermitten.frigga.runtime.entity.type.Type
import me.bristermitten.frigga.runtime.expression.Expression

class EqualityOperation(
    override val left: Expression,
    override val right: Expression
) : BinaryOperation {
    override val type: Type = BoolType

    override fun eval(stack: Stack, context: FriggaContext) {
        left.eval(stack, context)
        val leftValue = stack.pull()
        right.eval(stack, context)
        val rightValue = stack.pull()

        stack.push(leftValue == rightValue)
    }
}
