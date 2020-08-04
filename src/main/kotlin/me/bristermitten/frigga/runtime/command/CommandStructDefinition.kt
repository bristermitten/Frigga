package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.Property
import me.bristermitten.frigga.runtime.data.structure.Struct
import me.bristermitten.frigga.runtime.data.structure.Structure
import me.bristermitten.frigga.runtime.type.Type
import me.bristermitten.frigga.runtime.type.TypeProperty

data class CommandStructDefinition(
	val name: String,
	val parents: List<Type>,
	val properties: List<StructProperty>
) : Command()
{
	private var struct: Struct? = null
	override fun eval(stack: Stack, context: FriggaContext)
	{
		val values = properties.map {
			val value = it.value?.run {
				eval(stack, context)
				stack.pull()
			}

			TypeProperty((value?.type ?: it.type).obtain(context), Property(it.name, emptySet(), value))
		}

		val struct = Struct(name, parents.mapNotNull { it.obtain(context) as? Structure }, values)
		this.struct = struct
		struct.init()

		context.defineType(struct)
	}

	data class StructProperty(val name: String, val type: Type, val value: Command?)

	private fun Type.obtain(context: FriggaContext): () -> Type
	{
		return {
			val struct = struct
			if (struct != null && struct.name == name)
			{
				struct
			} else
			{
				reestablish(context)
			}
		}
	}
}
