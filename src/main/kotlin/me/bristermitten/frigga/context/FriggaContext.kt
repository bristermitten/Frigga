package me.bristermitten.frigga.context

import me.bristermitten.frigga.Function
import me.bristermitten.frigga.expression.Expression

open class FriggaContext(
    variables: Map<String, Expression> = emptyMap(),
    functions: Map<String, Function> = emptyMap(),
    val parent: FriggaContext? = GlobalContext
) {

    internal val variables = variables.toMutableMap()
    internal val functions = functions.toMutableMap()

    @Throws(IllegalArgumentException::class)
    fun resolveVariable(name: String): Any {
        val existing =
            variables[name] ?: parent?.resolveVariable(name) ?: throw IllegalArgumentException("No such variable $name")
        return if (existing is Expression) {
            existing.evaluate(this)
        } else existing
    }

    @Throws(IllegalArgumentException::class)
    fun registerVar(name: String, value: Expression) {
        val existing = variables[name]
        if (existing != null) {
            throw IllegalArgumentException("Cannot redefine variable $name")
        }
        variables[name] = value
    }

    fun function(name: String): Function {
        return functions[name] ?: parent?.function(name) ?: throw IllegalArgumentException("No such Function $name")
    }

    fun function(name: String, function: Function) {
        val existing = functions[name]
        if (existing != null) {
            throw IllegalArgumentException("Cannot redefine function $name")
        }

        functions[name] = function
    }

    override fun toString(): String {
        return "FriggaContext(vars=$variables)"
    }
}
