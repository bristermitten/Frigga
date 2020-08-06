package me.bristermitten.frigga.ast.transformer

import FriggaParser
import me.bristermitten.frigga.ast.AssignableExpression
import me.bristermitten.frigga.ast.Element
import me.bristermitten.frigga.ast.property.toModifier
import me.bristermitten.frigga.ast.statement.PropertyCreationStatement

/**
 * @author AlexL
 */
object PropertyCreationTransformer : NodeTransformer<FriggaParser.PropertyCreationContext>()
{
	override fun transformNode(node: FriggaParser.PropertyCreationContext): Element
	{
		val untyped = node.untypedPropertyDeclaration()

		return PropertyCreationStatement(
			node.extensionDefinition()?.type()?.text,
			untyped.ID().text,
			untyped.propertyModifier().map { it.toModifier() }.toSet(),
			null,
			Transformers.transform(node.assignableExpression()).element as AssignableExpression
		)
	}
}
