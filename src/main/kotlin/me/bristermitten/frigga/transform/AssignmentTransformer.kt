package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandAssignment

object AssignmentTransformer : NodeTransformer<FriggaParser.PropertyAssignmentContext>()
{

	override fun transformNode(node: FriggaParser.PropertyAssignmentContext): Command
	{
		with(node) {
			val access = this.propertyAccess()
			val upon = (access as? FriggaParser.ChildAccessContext)?.let(NodeTransformers::transform)
			val name = if (access is FriggaParser.DirectAccessContext)
			{
				access.text
			} else
			{
				access.text.substringAfterLast('.')
			}

			val typed = this.propertyType()
			val type = typed?.type()?.toType()

			val expression = assignableExpression()

			val value = NodeTransformers.transform(expression)

			return CommandAssignment(upon?.command, name, emptySet(), value, type)
		}
	}
}
