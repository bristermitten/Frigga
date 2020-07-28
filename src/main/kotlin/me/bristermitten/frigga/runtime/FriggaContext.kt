package me.bristermitten.frigga.runtime

import OutputType
import me.bristermitten.frigga.runtime.data.Property
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.data.function.Function
import me.bristermitten.frigga.runtime.type.CallerType
import me.bristermitten.frigga.runtime.type.StackType
import me.bristermitten.frigga.runtime.type.Type
import me.bristermitten.frigga.runtime.type.TypeInstance
import me.bristermitten.frigga.util.set

class FriggaContext(val name: String) {
    val stack = Stack()

    private var globalScope = loadGlobalScope()

    private val scope = ArrayDeque<FriggaScope>().apply {
        add(globalScope)
    }

    private val using = mutableSetOf<FriggaContext>()

    fun copy(): FriggaContext {
        return FriggaContext(name).also {
            it.globalScope = this.globalScope
            it.scope.clear()
            it.scope.addAll(this.scope)

            it.using.clear()
            it.using.addAll(this.using)
        }
    }

    private fun loadGlobalScope(): FriggaScope {
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

        return globalScope
    }

    internal fun findProperty(name: String): Property? {
        return findPropertyScope(name)?.second
    }

    internal fun findPropertyScope(name: String): Pair<FriggaScope, Property>? {
        for (scope in scope) {
            val property = scope.properties[name].firstOrNull()
            if (property != null) {
                return scope to property
            }
        }

        for (other in using) {
            val property = other.findPropertyScope(name)
            if (property != null) {
                return property
            }
        }
        return null
    }

    internal fun findType(name: String): Type? {
        for (scope in scope) {
            val type = scope.types[name]
            if (type != null) {
                return type
            }
        }

        for (other in using) {
            val type = other.findType(name)
            if (type != null) {
                return type
            }
        }
        return null
    }

    fun findTypeFunction(type: Type, value: TypeInstance, name: String, parameterTypes: List<Type>): Function? {
        val function = type.getFunction(name, parameterTypes) ?: return null
        var functionValue = value.properties[function]?.value as Function?

        if (functionValue != null) {
            return functionValue
        }

        for (other in using) {
            functionValue = other.findTypeFunction(type, value, name, parameterTypes)
            if (functionValue != null) {
                return functionValue
            }
        }
        return null
    }

    internal fun findFunction(type: Type? = null, name: String, parameterTypes: List<Type>): Function? {

        if (type != null) {
            val value = type.getFunction(name, parameterTypes)?.value
            return value as Function?
        }

        val functionParameter = findParameter(name)?.value as? Function
        if (functionParameter != null) {
            return functionParameter
        }
        val functionProperty = findProperty(name)?.value?.value as? Function
        if (functionProperty != null) {
            return functionProperty
        }

        //Constructors
        val constructorType = findType(name)

        if (constructorType != null) {
            return findFunction(constructorType, CONSTRUCTOR, parameterTypes)
        }
        for (other in using) {
            val function = other.findFunction(type, name, parameterTypes)
            if (function != null) {
                return function
            }
        }
        return null
    }

    internal fun defineProperty(property: Property, force: Boolean = false) {
        if (property.name in reservedNames && !force) {
            throw IllegalArgumentException("Cannot define property with reserved name ${property.name}")
        }
        scope[0].properties[property.name] = property
    }

    internal fun defineProperty(name: String, value: Value, forceReservedName: Boolean = false) {
        return defineProperty(
            Property(name, emptySet(), value),
            forceReservedName
        )
    }

    internal fun defineParameter(name: String, value: Value) {
        scope[0].parameters[name] = value
    }

    fun findParameter(name: String): Value? {
        for (scope in scope) {
            val param = scope.parameters[name]
            if (param != null) {
                return param
            }
        }
        return null
    }

    fun enterScope(name: String) {
        scope.addFirst(FriggaScope(name))
    }

    fun enterFunctionScope(name: String) {
        scope.addFirst(FriggaScope(name, true))
    }

    fun exitScope(): FriggaScope {
        return scope.removeFirst()
    }

    val deepestScope
        get() = scope.first()

    fun reset() {
        stack.clear()

        scope.forEach {
            it.properties.clear()
            it.types.clear()
        }

        scope.clear()
        globalScope = loadGlobalScope()
        scope += globalScope
    }

    fun defineType(type: Type) {
        scope[0].types[type.name] = type
    }

    fun use(namespace: FriggaContext) {
        using += namespace
    }

}
