package me.bristermitten.frigga.context

import me.bristermitten.frigga.expression.Expression

class FunctionContext(
    parent: FriggaContext,
    val parameters: Map<String, Expression>
) : FriggaContext(parent.variables, parent.functions, parent)
