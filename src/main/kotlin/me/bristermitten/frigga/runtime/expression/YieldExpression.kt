package me.bristermitten.frigga.runtime.expression

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.entity.type.Type

data class YieldExpression(
    override val type: Type,
    private val yields: (Stack, FriggaContext) -> Any
) : Expression {

    override fun eval(stack: Stack, context: FriggaContext) {
        when (val yielding = yields(stack, context)) {
            is LiteralExpression -> {
                stack.push(yielding.value)
            }
            is Expression -> {
                yielding.eval(stack, context)
            }
            else -> stack.push(yielding)
        }
    }
}
