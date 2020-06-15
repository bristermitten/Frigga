package me.bristermitten.frigga.ast.element.exp.value

import me.bristermitten.frigga.ast.element.exp.Expression

data class VarReference(
    val referencing: String
) : Expression
