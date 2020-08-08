package me.bristermitten.frigga.ast

import me.bristermitten.frigga.runtime.context.Context

/**
 * @author AlexL
 */
interface Element<T: Any>
{
	fun execute(context: Context): T
}
