package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.ast.element.*
import me.bristermitten.frigga.runtime.command.function.FunctionValue

class FriggaContext {
    val stack = Stack()

    private val globalScope = loadGlobalScope()
    val scope = ArrayDeque<FriggaScope>().apply {
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

    internal fun findFunction(type: Type? = null, name: String, parameterTypes: List<Type>): FunctionValue? {
        if (type != null) {
            val withName = type.getFunctions(name)
            val found = withName.firstOrNull {
                var index = 0
                it.signature.input.values.all { parameterType ->
                    parameterType.accepts(parameterTypes[index++])
                }
            }
            if (found != null) {
                return found
            }
        }
        for (scope in scope) {
            val property = scope.functions[name]
            if (property != null) {
                return property
            }
        }
        return null
    }

    internal fun defineProperty(property: Property) {
        if (property.name in reservedNames) {
            throw IllegalArgumentException("Cannot define property with reserved name ${property.name}")
        }
        scope[0].properties[property.name] = property
        val value = property.value.value
        if (value is FunctionValue) {
            scope[0].functions[property.name] = value
        }
    }

    internal fun defineProperty(name: String, value: Value) {
        return defineProperty(
            Property(name, emptySet(), value)
        )
    }

    fun enterScope(name: String) {
        scope.addFirst(FriggaScope(name))
    }

    fun leaveScope(): FriggaScope {
        return scope.removeFirst()
    }

    fun reset() {
        stack.clear()
        scope.forEach {
            it.functions.clear()
            it.properties.clear()
            it.types.clear()
        }
    }

}
