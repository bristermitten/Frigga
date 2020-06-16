package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.command.Command

class CommandPropertyReference(private val referencing: String) : Command() {
    override fun eval(stack: Stack, context: FriggaContext) {
        val found = context.findProperty(referencing)
        requireNotNull(found) {
            "No such property $referencing"
        }
        stack.push(found.value)
    }

    override fun toString(): String {
        return "CommandPropertyReference(referencing='$referencing')"
    }


}
