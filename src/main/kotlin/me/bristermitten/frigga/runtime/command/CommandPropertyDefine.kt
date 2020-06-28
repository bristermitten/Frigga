package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.ast.element.Modifier
import me.bristermitten.frigga.ast.element.Property
import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.Value

internal class CommandPropertyDefine(
    val name: String,
    val modifiers: Set<Modifier>,
    val value: Command
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        val existing = context.findProperty(name)

        if (existing != null) {
            value.eval(stack, context)
            val redefineTo = stack.pull() as Value //Pull irrespective of mutability to ensure no leftover values on the stack

            require(Modifier.MUTABLE in existing.modifiers) {
                "Attempting to redefine a non mutable property ${existing.name}"
            }
            require(existing.value.type.accepts(redefineTo.type)) {
                "Cannot reassign ${existing.name} of type ${existing.value.type} to value of type ${redefineTo.type}"
            }
            existing.value = redefineTo.type.coerceTo(redefineTo, existing.value.type)
            return
        }
        value.eval(stack, context)
        val propertyValue = stack.pull() as Value

        val property = Property(
            name,
            modifiers,
            propertyValue
        )

        context.defineProperty(property)
    }

    override fun toString(): String {
        return "CommandPropertyDefine(name='$name', value=${value})"
    }


}
