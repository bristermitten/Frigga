package me.bristermitten.frigga.runtime.expression.binary

import me.bristermitten.frigga.runtime.expression.Expression

interface BinaryOperation : Expression {
    val left: Expression
    val right: Expression
}
