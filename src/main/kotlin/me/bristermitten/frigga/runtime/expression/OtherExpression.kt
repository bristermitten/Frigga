package me.bristermitten.frigga.runtime.expression

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.entity.type.Type

data class OtherExpression(
    override val type: Type,
    private val onRun: (Stack, FriggaContext) -> Unit
) : Expression {
    override fun eval(stack: Stack, context: FriggaContext) {
        onRun(stack, context)
    }
}
