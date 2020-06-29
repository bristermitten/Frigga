package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.data.Modifier
import me.bristermitten.frigga.runtime.data.Property

data class CommandAssignment(
    val name: String,
    val modifiers: Set<Modifier>,
    val value: CommandNode
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {

        val existing = context.findPropertyScope(name)
        if (existing != null) {
            val existingProperty = existing.second

            if (existing.first == context.deepestScope) {
                require(Modifier.MUTABLE in existingProperty.modifiers) {
                    "Cannot redefine a non mutable property ${existingProperty.name}"
                }

                value.command.eval(stack, context)
                val propertyValue = stack.pull()

                require(existingProperty.value.type.accepts(propertyValue.type)) {
                    "Cannot reassign ${existingProperty.name} of type ${existingProperty.value.type} to value of type ${propertyValue.type}"
                }
                existingProperty.value = propertyValue.type.coerceTo(propertyValue, existingProperty.value.type)
                return
            }
        }

        value.command.eval(stack, context)
        val propertyValue = stack.pull()

        val property = Property(name, modifiers, propertyValue)
        context.defineProperty(property)
    }
}
