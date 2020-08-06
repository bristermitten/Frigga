package me.bristermitten.frigga.ast.statement

import me.bristermitten.frigga.ast.AssignableExpression
import me.bristermitten.frigga.ast.property.Modifier
import me.bristermitten.frigga.runtime.type.Type

/**
 * @author AlexL
 */
data class PropertyCreationStatement(
	val extensionReceiver: String?,
	val name: String,
	val modifiers: Set<Modifier>,
	private var type: Type?,
	val value: AssignableExpression
) : Statement
{
	override fun execute()
	{
		println("Created $name = ${value.evaluate()}")
	}
}
