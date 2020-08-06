package me.bristermitten.frigga.ast.transformer

import FriggaParser
import me.bristermitten.frigga.ast.Node

/**
 * @author AlexL
 */
object ExpressionTransformer : NodeTransformer<FriggaParser.ExpressionLineContext>()
{
	override fun transform(node: FriggaParser.ExpressionLineContext): Node
	{
		return Transformers.transform(node.assignableExpression())
	}
}
