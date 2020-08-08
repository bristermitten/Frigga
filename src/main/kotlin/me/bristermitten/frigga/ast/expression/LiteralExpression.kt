package me.bristermitten.frigga.ast.expression

import me.bristermitten.frigga.ast.AssignableExpression
import me.bristermitten.frigga.runtime.context.Context
import me.bristermitten.frigga.runtime.value.Value

/**
 * @author AlexL
 */
class LiteralExpression(
	val value: Value
) : AssignableExpression
{
	override fun evaluate(context: Context): Value
	{
		return value
	}
}
