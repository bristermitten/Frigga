package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.THIS_NAME
import me.bristermitten.frigga.runtime.type.TypeInstance

data class CommandBooleanNot(
    val inverting: Command
) : Command()
{

    override fun eval(stack: Stack, context: FriggaContext)
    {
        inverting.eval(stack, context)

        val toInvert = stack.pull()

        val value = toInvert.value
        val notFunction = if (value is TypeInstance)
        {
            context.findTypeFunction(toInvert.type, value, PREFIX_OPERATOR_NOT_NAME, emptyList())
        } else
        {
            context.findFunction(toInvert.type, PREFIX_OPERATOR_NOT_NAME, emptyList())
        }

        requireNotNull(notFunction) {
            "Type ${toInvert.type} does not define a function named 'not', cannot be inverted."
        }


        notFunction.call(toInvert, stack, context, emptyList())
    }
}
