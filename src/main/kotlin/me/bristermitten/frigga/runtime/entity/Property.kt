package me.bristermitten.frigga.runtime.entity

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.entity.type.NothingType
import me.bristermitten.frigga.runtime.entity.type.Type
import me.bristermitten.frigga.runtime.expression.Expression
import me.bristermitten.frigga.runtime.expression.OtherExpression
import me.bristermitten.frigga.runtime.expression.YieldExpression

data class Property(
    val propertyName: String,
    val mutable: Boolean,
    override val type: Type,
    var value: Expression
) : Expression {

    val getter: Function = Function(
        propertyName,
        listOf(YieldExpression(value.type) { _, _ -> value }),
        Signature(this.type, emptyMap())
    )

    override fun eval(stack: Stack, context: FriggaContext) = getter.eval(stack, context)

    val setter: Function? = run {
        if (mutable) {
            Function(
                "set",
                listOf(
                    OtherExpression(NothingType) { stack, _ ->
                        this.value = stack.pull() as me.bristermitten.frigga.runtime.expression.Expression
                    }
                ),
                Signature(
                    NothingType,
                    mapOf("value" to this.type)
                )
            )
        } else {
            null
        }
    }
}
