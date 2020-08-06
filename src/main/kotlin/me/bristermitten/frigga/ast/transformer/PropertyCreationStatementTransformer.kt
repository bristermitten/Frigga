package me.bristermitten.frigga.ast.transformer

import FriggaParser
import me.bristermitten.frigga.ast.Node

/**
 * @author AlexL
 */
object PropertyCreationStatementTransformer : NodeTransformer<FriggaParser.PropertyCreationStatementContext>()
{
	override fun transform(node: FriggaParser.PropertyCreationStatementContext): Node
	{
		return Transformers.transform(node.propertyCreation())
	}
}
