package me.bristermitten.frigga.runtime

import InputType
import OutputType
import me.bristermitten.frigga.runtime.data.Property
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.data.function.Function
import me.bristermitten.frigga.runtime.type.CallerType
import me.bristermitten.frigga.runtime.type.StackType
import me.bristermitten.frigga.runtime.type.Type
import me.bristermitten.frigga.runtime.type.TypeInstance
import me.bristermitten.frigga.util.set
import findType as findInternalType

class FriggaContext(val name: String)
{
	val stack = Stack()

	private var globalScope = loadGlobalScope()

	private val scope = ArrayDeque<FriggaScope>().apply {
		add(globalScope)
	}

	private val using = mutableSetOf<FriggaContext>()

	fun copy(): FriggaContext
	{
		return FriggaContext(name).apply {
			globalScope = this@FriggaContext.globalScope
			scope.clear()
			scope.addAll(this@FriggaContext.scope)

			using.clear()
			using.addAll(this@FriggaContext.using)
		}
	}

	private fun loadGlobalScope(): FriggaScope
	{
		val globalScope = FriggaScope("global")
		globalScope.properties[STACK_NAME] = Property(
			STACK_NAME,
			emptySet(),
			Value(
				StackType,
				stack
			)
		)
		globalScope.properties[CALLER_NAME] = Property(
			CALLER_NAME,
			emptySet(),
			Value(
				CallerType,
				Unit
			)
		)
		globalScope.properties[STDOUT_NAME] = Property(
			STDOUT_NAME,
			emptySet(),
			Value(
				OutputType,
				Unit
			)
		)
		globalScope.properties[STDIN_NAME] = Property(
			STDIN_NAME,
			emptySet(),
			Value(
				InputType,
				Unit
			)
		)

		return globalScope
	}

	internal fun findProperty(name: String): Property?
	{
		return findPropertyScope(name)?.second
	}

	internal fun findProperty(type: TypeInstance, name: String): Property?
	{
		val property = type.type.getProperty(name)
		val instanceProperty = type.properties[property]
		if (instanceProperty != null)
		{
			return Property(name, property?.property?.modifiers ?: emptySet(), instanceProperty)
		}
		return property?.property ?: findProperty(name)
	}

	private fun findPropertyScopes(name: String): List<Pair<FriggaScope, Property>>
	{
		val props = mutableListOf<Pair<FriggaScope, Property>>()
		for (scope in scope)
		{
			val properties = scope.properties[name]
			for (property in properties)
			{
				props.add(scope to property)
			}
		}

		for (other in using)
		{
			props.addAll(other.findPropertyScopes(name))
		}
		return props
	}

	internal fun findPropertyScope(name: String): Pair<FriggaScope, Property>?
	{
		for (scope in scope)
		{
			val property = scope.properties[name].firstOrNull()
			if (property != null)
			{
				return scope to property
			}
		}

		for (other in using)
		{
			val property = other.findPropertyScope(name)
			if (property != null)
			{
				return property
			}
		}
		return null
	}

	internal fun findType(name: String): Type?
	{
		val internalType = findInternalType(name) //global types
		if (internalType != null)
		{
			return internalType
		}

		for (scope in scope)
		{
			val type = scope.typeParameters[name] ?: scope.types[name]
			if (type != null)
			{
				return type
			}
		}

		for (other in using)
		{
			val type = other.findType(name)
			if (type != null)
			{
				return type
			}
		}
		return null
	}


	fun findExtensionFunction(type: Type, name: String, parameterTypes: List<Type>): Function?
	{
		return findFunctions(name)
			.filter { it.extensionType == type }
			.firstOrNull {
				it.signature.typesMatch(parameterTypes)
			}

	}

	fun findTypeFunction(type: Type, value: TypeInstance, name: String, parameterTypes: List<Type>): Function?
	{
		val property = type.getFunction(name, parameterTypes)
		if (property != null)
		{

			val functionValue = (value.properties[property]?.value ?: property.property.value?.value) as Function?

			if (functionValue != null)
			{
				return functionValue
			}
		}

		val extensionFunction = findExtensionFunction(type, name, parameterTypes)
		if (extensionFunction != null)
		{
			return extensionFunction
		}

		for (other in using)
		{
			val functionValue = other.findTypeFunction(type, value, name, parameterTypes)
			if (functionValue != null)
			{
				return functionValue
			}
		}
		return null
	}

	private fun findFunctions(name: String): List<Function>
	{
		val functionProperties = findPropertyScopes(name)

		return functionProperties.mapNotNull {
			it.second.value?.value
		}.filterIsInstance<Function>()
	}

	private fun findFunctionInType(type: Type, name: String, parameterTypes: List<Type>): Function?
	{
		val value = type.getFunction(name, parameterTypes)?.property?.value?.value as? Function
		if (value != null)
		{
			return value
		}

		val extensionFunction = findExtensionFunction(type, name, parameterTypes)
		if (extensionFunction != null)
		{
			return extensionFunction
		}
		return null
	}

	internal fun findFunction(type: Type? = null, name: String, parameterTypes: List<Type>): Function?
	{
		if (type != null)
		{
			return findFunctionInType(type, name, parameterTypes)
		}

		val functionParameter = findParameter(name)?.value as? Function
		if (functionParameter != null)
		{
			return functionParameter
		}


		val matching = findFunctions(name)
			.firstOrNull { it.signature.typesMatch(parameterTypes) }

		if (matching != null)
		{
			return matching
		}

		//Constructors
		val constructorType = findType(name) //If there is a type matching the function name (eg String)
		if (constructorType != null)
		{
			return findFunction(constructorType, CONSTRUCTOR, parameterTypes)
		}

		for (other in using)
		{
			val function = other.findFunction(type, name, parameterTypes)
			if (function != null)
			{
				return function
			}
		}
		return null
	}

	internal fun defineProperty(property: Property, force: Boolean = false)
	{
		if (property.name in reservedNames && !force)
		{
			throw IllegalArgumentException("Cannot define property with reserved name ${property.name}")
		}
		scope[0].properties[property.name] = property
	}

	internal fun defineProperty(name: String, value: Value, forceReservedName: Boolean = false)
	{
		return defineProperty(
			Property(name, emptySet(), value),
			forceReservedName
		)
	}

	internal fun defineParameter(name: String, value: Value)
	{
		scope[0].parameters[name] = value
	}

	internal fun defineTypeParameter(name: String, type: Type)
	{
		scope[0].typeParameters[name] = type
	}

	internal fun findTypeParameter(name: String): Type?
	{
		for (scope in scope)
		{
			val parameter = scope.typeParameters[name]
			if (parameter != null)
			{
				return parameter
			}
		}
		return null
	}


	fun findParameter(name: String): Value?
	{
		for (scope in scope)
		{
			val param = scope.parameters[name]
			if (param != null)
			{
				return param
			}
		}
		return null
	}

	fun enterScope(name: String)
	{
		scope.addFirst(FriggaScope(name))
	}

	fun enterFunctionScope(name: String)
	{
		scope.addFirst(FriggaScope(name, true))
	}

	fun exitScope(): FriggaScope
	{
		return scope.removeFirst()
	}

	val deepestScope
		get() = scope.first()

	fun reset()
	{
		stack.clear()

		scope.forEach {
			it.properties.clear()
			it.types.clear()
		}

		scope.clear()
		globalScope = loadGlobalScope()
		scope += globalScope
	}

	fun defineType(type: Type)
	{
		scope[0].types[type.name] = type
	}

	fun use(namespace: FriggaContext)
	{
		using += namespace
	}

}
