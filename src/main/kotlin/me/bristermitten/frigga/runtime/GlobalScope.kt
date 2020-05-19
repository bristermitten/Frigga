package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.runtime.entity.Function
import me.bristermitten.frigga.runtime.entity.Signature
import me.bristermitten.frigga.runtime.entity.type.AnyType
import me.bristermitten.frigga.runtime.expression.YieldExpression

object GlobalScope {

    fun initialize(scope: Scope) {
        scope.properties["yield"] = Function(
            "yield",
            listOf(YieldExpression(AnyType) { stack, friggaContext ->
                stack.pull()
//                stack.push(YieldExpression(yielding, AnyType))
            }),
            Signature(
                AnyType,
                mapOf("yielding" to AnyType)
            )
        ).toProperty()
    }
}
