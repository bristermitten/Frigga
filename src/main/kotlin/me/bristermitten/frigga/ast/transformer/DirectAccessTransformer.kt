package me.bristermitten.frigga.ast.transformer

import FriggaParser
import me.bristermitten.frigga.ast.Element
import me.bristermitten.frigga.ast.expression.PropertyAccessExpression

/**
 * @author AlexL
 */
object DirectAccessTransformer : NodeTransformer<FriggaParser.DirectAccessContext>()
{
	override fun transformNode(node: FriggaParser.DirectAccessContext): Element<*>?
	{
		return PropertyAccessExpression(node.structName().ID().text)
		//TODO support namespaced access too
	}
}
