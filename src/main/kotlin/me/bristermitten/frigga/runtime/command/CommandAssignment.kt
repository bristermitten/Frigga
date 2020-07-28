package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.data.Modifier
import me.bristermitten.frigga.runtime.data.Property
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.data.function.Function

data class CommandAssignment(
        val name: String,
        val modifiers: Set<Modifier>,
        val value: CommandNode
) : Command()
{

    override fun eval(stack: Stack, context: FriggaContext)
    {
        val existing = context.findPropertyScope(name)

        value.command.eval(stack, context)
        val propertyValue = stack.pull()

        if (existing == null)
        {
            defineProperty(context, name, modifiers, propertyValue)
            return
        }

        val existingProperty = existing.second
        val existingValue = existingProperty.value.value


        if (existing.first == context.deepestScope || context.deepestScope.isFunctionScope)
        { //Allow property shadowing

            val mutable = Modifier.MUTABLE in existingProperty.modifiers

            if (!mutable)
            {
                if (existingValue is Function && existingProperty.value.type.accepts(propertyValue.type))
                {
                    throw IllegalArgumentException("Function conflicts with existing function ${existingValue.name} ${existingValue.signature}")
                } else
                {
                    defineProperty(context, name, modifiers, propertyValue)
                    return
                }
            }

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

    private fun defineProperty(context: FriggaContext, name: String, modifiers: Set<Modifier>, value: Value)
    {
        val property = Property(name, modifiers, value)
        context.defineProperty(property)
    }
}
