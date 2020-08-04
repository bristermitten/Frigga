package me.bristermitten.frigga.runtime.data.structure

import me.bristermitten.frigga.runtime.CONSTRUCTOR
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.data.function.body
import me.bristermitten.frigga.runtime.data.function.signature
import me.bristermitten.frigga.runtime.type.TypeInstance
import me.bristermitten.frigga.runtime.type.TypeProperty

class Struct(
	name: String,
	override val parents: List<Structure>,
	val elements: List<TypeProperty>
) : Structure(name)
{

	fun init()
	{
		val constructorElements = elements.filter { it.property.value == null }

		defineFunction {
			this.name = CONSTRUCTOR
			signature {
				output = this@Struct
				input = constructorElements.map { it.property.name to it.type() }.toMap()
			}
			body { stack, friggaContext ->
				val newInstance = TypeInstance(
					this@Struct,
					elements.mapNotNull {
						val value = friggaContext.findParameter(it.property.name) ?: it.property.value
						if (value == null)
						{
							null
						} else
						{
							it to value
						}
					}.toMap().toMutableMap()
				)

				stack.push(Value(this@Struct, newInstance))
			}
		}

		elements.forEach(this::defineProperty)
	}

	override fun toString(): String
	{
		return name
	}

}
