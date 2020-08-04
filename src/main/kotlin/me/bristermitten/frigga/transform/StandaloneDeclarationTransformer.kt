package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandAssignment
import me.bristermitten.frigga.runtime.command.CommandDeclaration
import me.bristermitten.frigga.runtime.data.PropertyDeclaration
import me.bristermitten.frigga.runtime.type.AnyType
import me.bristermitten.frigga.transform.DeclarationTransformer.toModifier

object StandaloneDeclarationTransformer : NodeTransformer<FriggaParser.StandlonePropertyDeclarationStatementContext>()
{

	override fun transformNode(node: FriggaParser.StandlonePropertyDeclarationStatementContext): Command
	{

		val typedPropertyDeclaration = node.standalonePropertyDeclaration().typedPropertyDeclaration()
		return CommandDeclaration(
			PropertyDeclaration(
				typedPropertyDeclaration.propertyModifier()?.map { it.toModifier() }?.toSet() ?: emptySet(),
				typedPropertyDeclaration.ID().text,
				typedPropertyDeclaration.propertyType().type().toType()
			)
		)
	}
}
