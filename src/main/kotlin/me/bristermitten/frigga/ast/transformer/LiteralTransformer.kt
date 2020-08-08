package me.bristermitten.frigga.ast.transformer

import FriggaParser
import me.bristermitten.frigga.ast.Element
import me.bristermitten.frigga.ast.expression.LiteralExpression
import me.bristermitten.frigga.runtime.type.DecType
import me.bristermitten.frigga.runtime.type.IntType
import me.bristermitten.frigga.runtime.value.Value

/**
 * @author AlexL
 */
object LiteralTransformer : NodeTransformer<FriggaParser.LiteralContext>()
{
	override fun transformNode(node: FriggaParser.LiteralContext): Element<*>
	{
		val int = node.IntLiteral()
		if (int != null)
		{
			return LiteralExpression(Value(IntType, int.text.toLong()))
		}

		val dec = node.DecLiteral()
		if (dec != null)
		{
			return LiteralExpression(Value(DecType, dec.text.toDouble()))
		}

		throw UnsupportedOperationException(node.text)
	}
}
