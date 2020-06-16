package me.bristermitten.frigga.scope

import me.bristermitten.frigga.ast.element.Property

class FriggaContext {
    val stack = Stack()

    private val globalScope = FriggaScope("global")
    val scope = ArrayDeque<FriggaScope>().apply {
        add(globalScope)
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

    internal fun defineProperty(property: Property) {
        scope[0].properties[property.name] = property
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
