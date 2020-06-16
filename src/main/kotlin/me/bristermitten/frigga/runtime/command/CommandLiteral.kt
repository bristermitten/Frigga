package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.Value


class CommandLiteral(val value: Value) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        stack.push(value)
    }

    override fun toString(): String {
        return "CommandLiteral(value=$value)"
    }

}
