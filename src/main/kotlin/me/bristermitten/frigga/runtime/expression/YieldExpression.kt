package me.bristermitten.frigga.runtime.expression

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.entity.type.Type

data class YieldExpression(
    private val yields: Any,
    override val type: Type
) : Expression {
    override fun eval(stack: Stack, context: FriggaContext) {
        stack.push(yields)
    }
}
