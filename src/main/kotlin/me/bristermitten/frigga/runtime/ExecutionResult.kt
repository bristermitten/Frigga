package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.runtime.value.Value

/**
 * @author AlexL
 */
data class ExecutionResult(
	val leftoverStack: List<Value>
)
{
}
