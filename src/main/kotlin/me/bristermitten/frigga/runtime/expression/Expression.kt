package me.bristermitten.frigga.runtime.expression

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.entity.type.Type

interface Expression {
    fun initialize(context: FriggaContext) = Unit

    fun eval(stack: Stack, context: FriggaContext)

    val type: Type
}
