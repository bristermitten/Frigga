package me.bristermitten.frigga.ast.transformer

import FriggaParser
import me.bristermitten.frigga.ast.Node

/**
 * @author AlexL
 */
object StatementTransformer : NodeTransformer<FriggaParser.StatementLineContext>()
{
	override fun transform(node: FriggaParser.StatementLineContext): Node
	{
		return Transformers.transform(node.statement())
	}
}
