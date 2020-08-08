package me.bristermitten.frigga.runtime.property

import me.bristermitten.frigga.ast.property.Modifier
import me.bristermitten.frigga.runtime.type.Type
import me.bristermitten.frigga.runtime.value.Value

/**
 * @author AlexL
 */
data class Property(
	val name: String,
	val modifiers: Set<Modifier>,
	val expectedType: Type,
	var value: Value
)
