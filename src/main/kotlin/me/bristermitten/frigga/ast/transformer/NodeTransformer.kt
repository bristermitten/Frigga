package me.bristermitten.frigga.ast.transformer

import me.bristermitten.frigga.ast.Element
import me.bristermitten.frigga.ast.Node
import me.bristermitten.frigga.ast.getPosition
import org.antlr.v4.runtime.ParserRuleContext

/**
 * @author AlexL
 */
abstract class NodeTransformer<T : ParserRuleContext>
{
	protected open fun transformNode(node: T): Element? = null

	open fun transform(node: T): Node
	{
		val element = transformNode(node)
		requireNotNull(element) {
			"$javaClass did not return an Element. Should it have overridden transform instead of transformNode?"
		}
		return Node(node.text, node.getPosition(), element)
	}
}
