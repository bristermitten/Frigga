package me.bristermitten.frigga.ast.transformer

import me.bristermitten.frigga.ast.Node
import org.antlr.v4.runtime.ParserRuleContext

/**
 * @author AlexL
 */

object Transformers
{
	private val transformers = mutableMapOf<Class<out ParserRuleContext>, NodeTransformer<in ParserRuleContext>>()

	@Suppress("UNCHECKED_CAST")
	private inline fun <reified T : ParserRuleContext> registerTransformer(transformer: NodeTransformer<in T>)
	{
		val key = T::class.java
		transformers[key] = transformer as NodeTransformer<in ParserRuleContext>
	}

	init
	{
		registerTransformer(StatementTransformer)
		registerTransformer(ExpressionTransformer)
		registerTransformer(OtherExpressionTransformer)

		registerTransformer(PropertyCreationStatementTransformer)
		registerTransformer(PropertyCreationTransformer)

		registerTransformer(LiteralTransformer)
		registerTransformer(LiteralExpressionTransformer)

		registerTransformer(PropertyAccessExpressionTransformer)
		registerTransformer(DirectAccessTransformer)

	}

	fun transform(node: ParserRuleContext): Node
	{
		val nodeTransformer = transformers[node.javaClass]
		requireNotNull(nodeTransformer) {
			"No transformer for ${node.javaClass} \"${node.text}\""
		}
		return nodeTransformer.transform(node)
	}
}
