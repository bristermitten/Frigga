package me.bristermitten.frigga.ast.expression

import me.bristermitten.frigga.ast.Expression
import me.bristermitten.frigga.runtime.context.Context
import me.bristermitten.frigga.runtime.value.Value

/**
 * @author AlexL
 */
data class PropertyAccessExpression(
	private val accessing: String
) : Expression
{
	override fun evaluate(context: Context): Value
	{
		val property = requireNotNull(context.findFirstProperty(accessing)) {
			"No such property $accessing"
		}

		return property.value
	}
}
