package me.bristermitten.frigga.ast

import me.bristermitten.frigga.runtime.context.Context
import me.bristermitten.frigga.runtime.value.Value

/**
 * @author AlexL
 */
interface AssignableExpression : Element<Value>
{
	override fun execute(context: Context): Value
	{
		return evaluate(context)
	}

	fun evaluate(context: Context): Value
}
