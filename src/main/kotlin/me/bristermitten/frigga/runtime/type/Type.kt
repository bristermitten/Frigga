package me.bristermitten.frigga.runtime.type

/**
 * @author AlexL
 */
open class Type(
	val name: String,
	val parent: Type? = AnyType
)
{
	open fun accepts(other: Type): Boolean
	{
		return other == this || other.isSubTypeOf(this)
	}

	open fun isSubTypeOf(other: Type): Boolean
	{
		var parent: Type? = this.parent
		do
		{
			if (parent == other)
			{
				return true
			}
			parent = parent?.parent
		} while (parent != null)
		return false
	}

	override fun toString(): String
	{
		return "Type($name)"
	}
}
