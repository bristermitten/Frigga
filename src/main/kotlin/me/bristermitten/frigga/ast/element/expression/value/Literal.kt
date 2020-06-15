package me.bristermitten.frigga.ast.element.expression.value

import me.bristermitten.frigga.ast.element.DecType
import me.bristermitten.frigga.ast.element.IntType
import me.bristermitten.frigga.ast.element.Type
import me.bristermitten.frigga.ast.element.expression.Expression

sealed class Literal<T>(val type: Type, val value: T) : Expression {
    override fun toString(): String {
        return value.toString()
    }
}

class IntLiteral(value: Long) : Literal<Long>(IntType, value)
class DecLiteral(value: Double) : Literal<Double>(DecType, value)
