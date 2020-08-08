package me.bristermitten.frigga.ast.transformer

import FriggaParser
import me.bristermitten.frigga.ast.Element
import me.bristermitten.frigga.ast.Node

/**
 * @author AlexL
 */
object PropertyAccessExpressionTransformer : NodeTransformer<FriggaParser.PropertyAccessExpressionContext>()
{
	override fun transform(node: FriggaParser.PropertyAccessExpressionContext): Node
	{
		return Transformers.transform(node.propertyAccess())
	}
}
