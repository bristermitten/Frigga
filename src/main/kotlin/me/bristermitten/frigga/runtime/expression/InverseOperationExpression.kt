package me.bristermitten.frigga.runtime.expression

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.entity.type.Type

data class InverseOperationExpression(
    val inverting: Expression
) : Expression {
    override fun eval(stack: Stack, context: FriggaContext) {
        inverting.eval(stack, context)
        val invert =
            stack.pull() as? BoolLiteralExpression ?: throw IllegalArgumentException("Can only invert Bool type.")
        stack.push(BoolLiteralExpression(!invert.value))
    }

    override val type: Type = inverting.type
}
