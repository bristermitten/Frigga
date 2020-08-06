package me.bristermitten.frigga.ast.property

/**
 * @author AlexL
 */
enum class Modifier
{
	MUTABLE,
	STATEFUL,
	SECRET,
	NATIVE
}

fun FriggaParser.PropertyModifierContext.toModifier(): Modifier
{
	if (this.MUTABLE() != null)
	{
		return Modifier.MUTABLE
	}
	if (this.NATIVE() != null)
	{
		return Modifier.NATIVE
	}
	if (this.SECRET() != null)
	{
		return Modifier.SECRET
	}
	throw UnsupportedOperationException(text)
}

