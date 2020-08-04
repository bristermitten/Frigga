package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandStructDefinition
import me.bristermitten.frigga.runtime.type.AnyType

object StructDeclarationTransformer : NodeTransformer<FriggaParser.StructDeclarationContext>()
{

	override fun transformNode(node: FriggaParser.StructDeclarationContext): Command
	{
		return CommandStructDefinition(

			node.ID().text,
			emptyList(),
			node.structBody().statement()
				.map {
					when (it)
					{
						is FriggaParser.PropertyDeclarationStatementContext ->
						{
							val typed = it.propertyDeclaration().typedPropertyDeclaration()
							val type = typed?.propertyType()?.type()?.toType()

							val name =
								typed?.ID()?.text ?: it.propertyDeclaration().untypedPropertyDeclaration().ID().text
							CommandStructDefinition.StructProperty(
								name,
								type ?: AnyType,
								null
							)
						}
						is FriggaParser.StandlonePropertyDeclarationStatementContext ->
						{
							val typedPropertyDeclaration = it.standalonePropertyDeclaration().typedPropertyDeclaration()
							CommandStructDefinition.StructProperty(
								typedPropertyDeclaration.ID().text,
								typedPropertyDeclaration.propertyType().type().toType(),
								null
							)
						}
						is FriggaParser.PropertyAssignmentStatementContext ->
						{
							val assignment = it.propertyAssignment()
							val value = assignment.assignableExpression()

							val type = assignment.propertyType()?.type()?.toType()

							return@map CommandStructDefinition.StructProperty(
								assignment.propertyAccess().text,
								type ?: AnyType,
								value?.let(NodeTransformers::transform)?.command
							)
						}
						else ->
						{
							throw IllegalArgumentException("Illegal statement inside struct ${it.text} ${it.javaClass}")
						}
					}
				})
	}
}
