package me.bristermitten.frigga.type

import me.bristermitten.frigga.*
import me.bristermitten.frigga.Function
import me.bristermitten.frigga.context.FriggaContext
import me.bristermitten.frigga.context.FunctionContext
import kotlin.reflect.KClass

class JVMType(kClass: KClass<*>) : Type {
    override val name: String = kClass.simpleName!!
    override val isArray = kClass.java.isArray
    override fun extends(potentialParent: Type): Boolean {
        return false //TODO
    }

    override val functions: List<Function> = kClass.java.methods.map {
        Function(
            listOf(
                object : Statement {
                    override fun execute(context: FriggaContext) {
                        val functionContext = context as FunctionContext
                        val params = functionContext.parameters.values.toList()
                        val parameters = it.parameters.mapIndexed { index, parameter ->
                            params[index]
                        }
                        parameters.forEach { expr -> expr.evaluate(functionContext) }
                    }

                    override val position: Position? = null
                }
            ),
            FunctionSignature(
                it.parameters.map { param ->
                    FunctionParameter(it.name, Types.typeOf(it.returnType.simpleName))
                }
            ),
            null
        )
    }
}
