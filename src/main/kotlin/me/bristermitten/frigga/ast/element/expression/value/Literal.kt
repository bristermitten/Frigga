package me.bristermitten.frigga.ast.element.expression.value

import me.bristermitten.frigga.ast.element.*
import me.bristermitten.frigga.ast.element.expression.Expression

sealed class Literal<T : Any>(val type: Type, val value: T) : Expression {
    override fun toString(): String {
        return value.toString()
    }
}

class IntLiteral(value: Long) : Literal<Long>(IntType, value)
class DecLiteral(value: Double) : Literal<Double>(DecType, value)
class StringLiteral(value: String) : Literal<String>(StringType, value)
class CharLiteral(value: Char) : Literal<Char>(CharType, value)

