package me.bristermitten.frigga.ast.element.expression.value

import me.bristermitten.frigga.runtime.*
import me.bristermitten.frigga.runtime.command.Command

sealed class Literal<T : Any>(val value: Value) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        stack.push(value)
    }

    override fun toString(): String {
        return value.toString()
    }
}

class IntLiteral(value: Long) : Literal<Long>(intValue(value))
class DecLiteral(value: Double) : Literal<Double>(decValue(value))
class StringLiteral(value: String) : Literal<String>(stringValue(value))
class CharLiteral(value: Char) : Literal<Char>(charValue(value))

