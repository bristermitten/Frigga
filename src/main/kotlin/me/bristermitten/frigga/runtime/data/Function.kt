package me.bristermitten.frigga.runtime.data

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.type.Type

data class Function(
    val name: String,
    val signature: Signature,
    val content: List<CommandNode>
) {
    fun call(stack: Stack, context: FriggaContext, params: Map<String, Value>) {
        context.enterScope(name)

        val params = signature.params.mapValues {

            val param = requireNotNull(params[it.key]) {
                "No value provided for parameter ${it.key}"
            }

            require(it.value.accepts(param.type)) {
                "Cannot use value of Type ${param.type} in place of ${it.value} for parameter ${it.key}"
            }
            param
        }

        params.forEach {
            context.defineProperty(it.key, it.value)
        }

        content.forEach {
            it.command.eval(stack, context)
        }

        context.exitScope()
    }
}

data class Signature(
    val typeSignature: Map<String, Type>,
    val params: Map<String, Type>,
    val returned: Type
)
