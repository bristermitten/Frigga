package me.bristermitten.frigga.ast.statement

import me.bristermitten.frigga.ast.AssignableExpression
import me.bristermitten.frigga.ast.property.Modifier
import me.bristermitten.frigga.ast.type.ASTTypeRepresentation
import me.bristermitten.frigga.runtime.context.Context
import me.bristermitten.frigga.runtime.property.Property

/**
 * @author AlexL
 */
data class PropertyCreationStatement(
	val extensionReceiver: String?,
	val name: String,
	val modifiers: Set<Modifier>,
	private var type: ASTTypeRepresentation?,
	val value: AssignableExpression
) : Statement
{
	override fun execute(context: Context)
	{
		val existing = context.findFirstProperty(name)
		if (existing != null)
		{
			return PropertyAssignmentStatement().execute(context)
		}
		val value = value.evaluate(context)

		val expectedType = type?.toType(context)
		//Perform simple type checking
		if (expectedType != null)
		{
			require(expectedType.accepts(value.type)) {
				"Type ${value.type} cannot be assigned to property of type $expectedType"
			}
		}

		val propertyType = expectedType ?: value.type

		val property = Property(
			name,
			modifiers,
			propertyType,
			value
		)

		context.defineProperty(property)
	}
}
