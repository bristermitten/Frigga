package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.runtime.entity.Function
import me.bristermitten.frigga.runtime.entity.Property
import me.bristermitten.frigga.runtime.entity.type.IntType
import me.bristermitten.frigga.runtime.expression.Expression

class FriggaContext {
    val stack = Stack()

    private val globalScope = Scope("global")
    private val scope = ArrayDeque<Scope>()

    init {
        scope.add(globalScope)
    }

    fun getProperty(name: String): Property? {
        for (scope in scope) {
            val foundFunction = scope.properties[name]
            if (foundFunction != null) {
                return foundFunction
            }
        }
        return null
    }

    fun enterScope(scope: Scope) {
        this.scope.addFirst(scope)
    }

    private fun defineFunction(function: Function) {
        scope.last().properties[function.name] = Property(function.name, false, function.type, function)
    }

    fun assign(name: String? = null, expression: Expression) {
        when (expression) {
            is Function -> {
                defineFunction(expression.copy(name = name ?: expression.name))
            }
            is Property -> {
                scope.last().properties[expression.propertyName] = expression
//                defineFunction(expression.getter)
//                expression.setter?.let(this::defineFunction)
            }
            else -> {
                if (name == null) {
                    throw IllegalArgumentException("Name must be provided for Literals!")
                }
                assign(
                    name, Property(
                        name,
                        false,
                        IntType,
                        expression
                    )
                )
            }
        }
    }

    fun addParameterValue(paramName: String, value: Any) {
        scope.first().paramValues[paramName] = value
    }

    fun currentScope() = scope.first()

    fun exitScope() {
        scope.removeFirst()
    }

    fun getParameter(paramName: String): Any? {
        for (scope in scope) {
            val foundFunction = scope.paramValues[paramName]
            if (foundFunction != null) {
                return foundFunction
            }
        }
        return null
    }

    override fun toString(): String {
        return "FriggaContext(stack=$stack, scope=$scope)"
    }
}
