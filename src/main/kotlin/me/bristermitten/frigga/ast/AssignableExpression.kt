package me.bristermitten.frigga.ast

import me.bristermitten.frigga.runtime.value.Value

/**
 * @author AlexL
 */
interface AssignableExpression : Element
{
	override fun execute()
	{
		evaluate()
	}

	fun evaluate(): Value
}
