package me.bristermitten.frigga.expression

interface BinaryExpression : Expression {
    val left: Expression
    val right: Expression
}
