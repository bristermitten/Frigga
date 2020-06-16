package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack

abstract class Command {
    abstract fun eval(stack: Stack, context: FriggaContext)
}
