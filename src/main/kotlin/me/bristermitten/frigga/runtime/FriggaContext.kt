package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.ast.element.*
import me.bristermitten.frigga.ast.element.Property
import me.bristermitten.frigga.runtime.Value
import me.bristermitten.frigga.runtime.command.function.FunctionValue

class FriggaContext {
    val stack = Stack()

    private val globalScope = loadGlobalScope()
    val scope = ArrayDeque<FriggaScope>().apply {
        add(globalScope)
    }

    private fun loadGlobalScope(): FriggaScope {
        val globalScope = FriggaScope("global")
        globalScope.properties["__stack"] = Property(
            "__stack",
            emptySet(),
            Value(
                JVMType(Stack::class.java),
                stack
            )
        )
        globalScope.properties["__caller"] = Property(
            "__caller",
            emptySet(),
            Value(
                CallerType,
                Unit
            )
        )
        globalScope.properties["__stdout"] = Property(
            "__stdout",
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

    internal fun findFunction(type: Type? = null, name: String): FunctionValue? {
        if (type != null) {
            val inType = type.functions[name]
            if (inType != null) {
                return inType
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
