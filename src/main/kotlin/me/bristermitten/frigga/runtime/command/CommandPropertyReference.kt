package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack

data class CommandPropertyReference(
    val referencing: String
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        val prop = context.findProperty(referencing)

        requireNotNull(prop) {
            "No such property $referencing"
        }

        stack.push(prop.value)
    }
}
