package me.bristermitten.frigga

import me.bristermitten.frigga.context.FriggaContext
import me.bristermitten.frigga.context.FunctionContext
import me.bristermitten.frigga.expression.Expression
import me.bristermitten.frigga.type.Type

data class Function(
    val contents: List<Statement>,
    val signature: FunctionSignature,
    override val position: Position?
) : Expression {

    override fun evaluate(context: FriggaContext): Any {
        if (context !is FunctionContext) {
            throw IllegalArgumentException("Function requires a FunctionContext")
        }
        return evaluate(context)
    }

    private fun evaluate(context: FunctionContext): Any {
        return contents.map { it.execute(context) }.lastOrNull() ?: Unit
    }
}

data class FunctionSignature(
    val params: List<FunctionParameter>
)

data class FunctionParameter(
    val name: String,
    val type: Type
)
