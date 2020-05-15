package me.bristermitten.frigga.expression

import me.bristermitten.frigga.context.FriggaContext
import me.bristermitten.frigga.Statement

interface Expression : Statement {
    override fun execute(context: FriggaContext) {
        evaluate(context)
    }

    fun evaluate(context: FriggaContext): Any
}
