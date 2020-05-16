package me.bristermitten.frigga

import me.bristermitten.frigga.context.FriggaContext
import me.bristermitten.frigga.context.FunctionContext
import me.bristermitten.frigga.expression.Expression
import me.bristermitten.frigga.rewrite.type.Type

interface Statement : Node {
    fun execute(context: FriggaContext)
}

data class FunctionCall(
    val functionName: String,
    override val position: Position?,
    val params: List<Expression>
) : Expression {
    override fun evaluate(context: FriggaContext) {
        val function = context.function(functionName)
        val functionContext = FunctionContext(
            context,
            params.mapIndexed { index, expression ->
                function.signature.params[index].name to expression
            }.toMap()
        )
        function.evaluate(functionContext)
    }
}

sealed class NumberLit : Expression {
    abstract override fun evaluate(context: FriggaContext): Number
}

data class IntLit(val value: String, override val position: Position?) : NumberLit() {
    override fun evaluate(context: FriggaContext) = value.toInt()
}

data class DecLit(val value: String, override val position: Position?) : NumberLit() {
    override fun evaluate(context: FriggaContext) = value.toDouble()
}

data class VarReference(val varName: String, override val position: Position?) : Expression {
    override fun evaluate(context: FriggaContext) = context.resolveVariable(varName)
}

data class DeclAssign(
    val varName: String,
    val type: Type,
    val value: Expression,
    override val position: Position?
) : Statement {

    override fun execute(context: FriggaContext) {
        if (value is Function) {
            context.function(varName, value)
        }
        context.registerVar(varName, value)
    }
}

