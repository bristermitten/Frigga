package me.bristermitten.frigga.ast.transformer

import FriggaParser
import me.bristermitten.frigga.ast.Node

/**
 * @author AlexL
 */
object OtherExpressionTransformer : NodeTransformer<FriggaParser.OtherExpressionContext>()
{
	override fun transform(node: FriggaParser.OtherExpressionContext): Node
	{
		return Transformers.transform(node.expression())
	}
}
