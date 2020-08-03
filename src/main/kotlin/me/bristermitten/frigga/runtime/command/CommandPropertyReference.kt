package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.type.typeValue

data class CommandPropertyReference(
    val referencing: String
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        val prop = context.findParameter(referencing) ?:
        context.findProperty(referencing)?.value ?:
        context.findType(referencing)?.let(::typeValue)

        requireNotNull(prop) {
            "No such property or type $referencing"
        }

        stack.push(prop)
    }
}
