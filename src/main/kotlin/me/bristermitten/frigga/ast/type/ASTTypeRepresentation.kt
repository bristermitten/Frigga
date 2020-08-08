package me.bristermitten.frigga.ast.type

import me.bristermitten.frigga.runtime.context.Context
import me.bristermitten.frigga.runtime.type.NothingType
import me.bristermitten.frigga.runtime.type.Type

/**
 * @author AlexL
 */
sealed class ASTTypeRepresentation(
	val text: String
)
{
	abstract fun toType(context: Context): Type
}

class ASTNothingType : ASTTypeRepresentation("_")
{
	override fun toType(context: Context): Type = NothingType

}

class ASTSimpleType(text: String) : ASTTypeRepresentation(text)
{
	override fun toType(context: Context): Type = requireNotNull(context.findFirstType(text)) {
		"No such type $text"
	}
}
