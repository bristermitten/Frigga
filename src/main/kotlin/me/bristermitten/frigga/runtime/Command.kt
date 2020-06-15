package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.scope.FriggaContext
import me.bristermitten.frigga.scope.Stack

abstract class Command {
    abstract fun eval(stack: Stack, context: FriggaContext)
}
