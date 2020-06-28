package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack

class CommandPropertyReference(private val referencing: String) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        var found = context.findProperty(referencing)

        if (found == null) {
            val type = context.findType(referencing)
            requireNotNull(type) {
                "No such property or type $referencing"
            }

            found = type.staticProperty
        }
        stack.push(found.value)
    }

    override fun toString(): String {
        return "CommandPropertyReference(referencing='$referencing')"
    }


}
