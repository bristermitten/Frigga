package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.scope.FriggaContext
import me.bristermitten.frigga.scope.Stack

class CommandPropertyReference(val referencing: String) : Command() {
    override fun eval(stack: Stack, context: FriggaContext) {
        val found = context.findProperty(referencing)
        requireNotNull(found) {
            "No such property $referencing"
        }
        stack.push(found.value)
    }
}
