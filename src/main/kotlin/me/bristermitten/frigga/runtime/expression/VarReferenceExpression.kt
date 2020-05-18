package me.bristermitten.frigga.runtime.expression

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.entity.Property
import me.bristermitten.frigga.runtime.entity.type.Type

data class VarReferenceExpression(private val varName: String) : Expression {
    private lateinit var referencing: Property

    override fun initialize(context: FriggaContext) {
        referencing = requireNotNull(context.getProperty(varName))
        {
            "Variable $varName has not been defined"
        }
    }

    private var initialized = false

    override fun eval(stack: Stack, context: FriggaContext) {
        if (!initialized) {
            initialize(context)
        }
        stack.push(referencing.value)
    }

    override val type: Type
        get() = referencing.type

}
