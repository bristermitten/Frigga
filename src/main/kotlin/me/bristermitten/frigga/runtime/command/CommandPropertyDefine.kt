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
        val existing = context.findPropertyScope(name)


        value.eval(stack, context)
        //Pull irrespective of mutability to ensure no leftover values on the stack
        val propertyValue = stack.pull() as Value


        if (existing != null) {
            val existingProperty = existing.second
            if (existing.first == context.deepestScope) { //allow property shadowing
                require(Modifier.MUTABLE in existingProperty.modifiers) {
                    "Cannot redefine a non mutable property ${existingProperty.name}"
                }
                require(existingProperty.value.type.accepts(propertyValue.type)) {
                    "Cannot reassign ${existingProperty.name} of type ${existingProperty.value.type} to value of type ${propertyValue.type}"
                }
                existingProperty.value = propertyValue.type.coerceTo(propertyValue, existingProperty.value.type)
                return
            }
        }

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
