package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.CommandNode

data class CommandAccess(
    val upon: CommandNode,
    val property: String
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        throw UnsupportedOperationException("This command should never be evalutated")
        //This command should never be
    }
}
