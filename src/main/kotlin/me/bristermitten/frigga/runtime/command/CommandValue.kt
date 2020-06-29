package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.Value

data class CommandValue(val value: Value) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        stack.push(value)
    }
}
