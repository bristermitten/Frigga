package me.bristermitten.frigga.runtime.expression

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.entity.type.*

abstract class LiteralExpression(open val value: Any, override val type: Type) : Expression {
    override fun eval(stack: Stack, context: FriggaContext) {
        stack.push(value)
    }
}

data class IntLiteralExpression(override val value: Int) : LiteralExpression(value, IntType)

data class DoubleLiteralExpression(override val value: Double) : LiteralExpression(value, DoubleType)

data class BoolLiteralExpression(override val value: Boolean) : LiteralExpression(value, BoolType)

data class StringLiteralExpression(override val value: String) : LiteralExpression(value, StringType)
