package me.bristermitten.frigga.ast.transformer

import FriggaParser
import me.bristermitten.frigga.ast.Node

/**
 * @author AlexL
 */
object LiteralExpressionTransformer : NodeTransformer<FriggaParser.LiteralExpressionContext>()
{
	override fun transform(node: FriggaParser.LiteralExpressionContext): Node
	{
		return Transformers.transform(node.literal())
	}
}
