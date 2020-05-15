package me.bristermitten.frigga.context

import me.bristermitten.frigga.*
import me.bristermitten.frigga.Function
import me.bristermitten.frigga.type.AnyType

object GlobalContext : FriggaContext(parent = null) {
    init {
        function("print", Function(
            listOf(
                object : Statement {
                    override fun execute(context: FriggaContext) {
                        println((context as FunctionContext).parameters["value"]?.evaluate(context))
                    }

                    override val position: Position?
                        get() = null

                }
            ),
            FunctionSignature(
                listOf(
                    FunctionParameter("value", AnyType)
                )
            ),
            null
        ))
    }
}
