package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.runtime.data.*
import me.bristermitten.frigga.runtime.data.Function
import me.bristermitten.frigga.runtime.type.CallerType
import me.bristermitten.frigga.runtime.type.JVMType
import me.bristermitten.frigga.runtime.type.OutputType
import me.bristermitten.frigga.runtime.type.Type

class FriggaContext {
    val stack = Stack()

    private var globalScope = loadGlobalScope()

    private val scope = ArrayDeque<FriggaScope>().apply {
        add(globalScope)
    }

    private fun loadGlobalScope(): FriggaScope {
        val globalScope = FriggaScope("global")
        globalScope.properties[STACK_NAME] = Property(
            STACK_NAME,
            emptySet(),
            Value(
                JVMType(Stack::class.java),
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
        for (scope in scope) {
            val property = scope.properties[name]
            if (property != null) {
                return property
            }
        }
        return null
    }

    internal fun findPropertyScope(name: String): Pair<FriggaScope, Property>? {
        for (scope in scope) {
            val property = scope.properties[name]
            if (property != null) {
                return scope to property
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
        return null
    }

    internal fun findFunction(type: Type? = null, name: String, parameterTypes: List<Type>): Function? {
        if (type != null) {
            val withName = type.getFunctions(name)
            val found = withName.firstOrNull {
                var index = 0
                it.signature.params.values.all { parameterType ->
                    parameterType.accepts(parameterTypes[index++])
                }
            }
            if (found != null) {
                return found
            }
        }
        for (scope in scope) {
            val function = scope.functions[name]
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
        val value = property.value.value
        if (value is Function) {
            scope[0].functions[property.name] = value
        }
    }

    internal fun defineProperty(name: String, value: Value, forceReservedName: Boolean = false) {
        return defineProperty(
            Property(name, emptySet(), value),
            forceReservedName
        )
    }

    fun enterScope(name: String) {
        scope.addFirst(FriggaScope(name))
    }

    fun exitScope(): FriggaScope {
        return scope.removeFirst()
    }

    val deepestScope
        get() = scope.first()

    fun reset() {
        stack.clear()

        scope.forEach {
//            it.functions.clear()
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

}
