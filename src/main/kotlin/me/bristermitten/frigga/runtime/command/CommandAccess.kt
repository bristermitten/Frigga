package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.type.TypeInstance

data class CommandAccess(
	val upon: CommandNode,
	val property: String
) : Command()
{

	override fun eval(stack: Stack, context: FriggaContext)
	{

		upon.command.eval(stack, context)
		val accessingFrom = stack.pull()

		val typeProperty = accessingFrom.type.getProperty(property)

		requireNotNull(typeProperty) {
			"No such property $property"
		}
		val typeValue = accessingFrom.value as TypeInstance

		val value = context.findProperty(typeValue, property)?.value

		requireNotNull(value) {
			"TypeInstance $typeValue did not contain a value for property $property"
		}
		stack.push(value)

	}
}
