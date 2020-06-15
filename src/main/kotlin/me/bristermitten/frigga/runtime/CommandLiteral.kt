package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.scope.FriggaContext
import me.bristermitten.frigga.scope.Stack

class CommandLiteral(val value: Value) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        stack.push(value)
    }

}
