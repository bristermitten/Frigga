package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.data.Modifier

data class CommandAssignment(
    val name: String,
    val modifiers: Set<Modifier>,
    val value: CommandNode
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        value.command.eval(stack, context)
        val propertyValue = stack.pull()

        context.defineProperty(name, propertyValue)
    }
}
