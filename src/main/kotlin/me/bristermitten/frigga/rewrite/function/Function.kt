package me.bristermitten.frigga.rewrite.function

import me.bristermitten.frigga.expression.Expression
import me.bristermitten.frigga.rewrite.Modifier

interface Function : Expression{
    val name: String
    val signature: FunctionSignature
    val modifiers: Set<Modifier>
}
