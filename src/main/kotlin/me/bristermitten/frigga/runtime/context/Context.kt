package me.bristermitten.frigga.runtime.context

import me.bristermitten.frigga.runtime.property.Property
import me.bristermitten.frigga.runtime.type.*
import me.bristermitten.frigga.util.set

/**
 * @author AlexL
 */
class Context
{
	private val globalScope = Scope("GLOBAL")
	private val scope = ArrayDeque<Scope>()

	init
	{
		scope.addFirst(globalScope)
		loadBaseTypes()
	}

	private fun loadBaseTypes()
	{
		//Load types
		fun Type.loadGlobal() = globalScope.types.put(name, this)
		AnyType.loadGlobal()
		NothingType.loadGlobal()
		IntType.loadGlobal()
		DecType.loadGlobal()
	}

	fun defineProperty(property: Property)
	{
		scope[0].properties[property.name] = property
	}

	fun findFirstProperty(name: String): Property?
	{
		return scope.asSequence().mapNotNull {
			it.properties[name].firstOrNull()
		}.firstOrNull()
	}

	fun defineType(type: Type)
	{
		scope[0].types[type.name] = type
	}

	fun findFirstType(name: String): Type?
	{
		return scope.asSequence().mapNotNull {
			it.types[name]
		}.firstOrNull()
	}
}


