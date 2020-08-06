package me.bristermitten.frigga.ast.expression

import me.bristermitten.frigga.ast.AssignableExpression
import me.bristermitten.frigga.runtime.value.Value

/**
 * @author AlexL
 */
class LiteralExpression(
	val value: Value
) : AssignableExpression
{
	override fun evaluate(): Value
	{
		return value
	}
}
