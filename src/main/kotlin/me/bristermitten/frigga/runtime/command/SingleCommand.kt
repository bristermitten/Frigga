package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack

private class SingleCommand(
    val onProcess: (Stack, FriggaContext) -> Unit
) : Command() {
    override fun eval(stack: Stack, context: FriggaContext) {
        onProcess(stack, context)
    }
}

fun singleCommand( onProcess: (Stack, FriggaContext) -> Unit) : List<Command> = listOf(SingleCommand(onProcess))
