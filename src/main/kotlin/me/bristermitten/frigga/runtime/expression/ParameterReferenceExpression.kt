package me.bristermitten.frigga.runtime.expression

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.entity.type.NothingType
import me.bristermitten.frigga.runtime.entity.type.Type


data class ParameterReferenceExpression(private val paramName: String) :
    Expression {

    override fun eval(stack: Stack, context: FriggaContext) {
        val param = requireNotNull(context.getParameter(paramName))
        {
            "Parameter $paramName has not been defined"
        }
        stack.push(param)
    }

    override val type: Type = NothingType
}
