package me.bristermitten.frigga.runtime.expression

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.entity.type.Type
import me.bristermitten.frigga.runtime.expression.binary.BinaryOperation

data class AssignmentExpression(
    private val name: String,
    private var value: Expression,
    private val expectedType: Type?
) : Expression {

    override lateinit var type: Type

    override fun initialize(context: FriggaContext) {
        val expected = expectedType
        this.type = if (expected != null) {
            expected
        } else {
            value.initialize(context)
            value.type
        }
    }

    override fun eval(stack: Stack, context: FriggaContext) {
        if (!type.matches(value.type)) {
            throw IllegalArgumentException("Cannot assign ${value.type} to Variable of $type")
        }
        if (value is BinaryOperation) {
            value.eval(stack, context)
            value = stack.pull() as Expression
        }

        context.assign(name, value)
    }

}
