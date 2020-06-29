package me.bristermitten.frigga.runtime.data.function

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.type.Type

data class Function(
    val name: String,
    val signature: Signature,
    val content: List<Command>
) {

    fun call(stack: Stack, context: FriggaContext, params: List<Value>) {
        val namedParams = signature.params.entries.mapIndexed { index, entry ->
            entry.key to params[index]
        }.toMap()

        call(stack, context, namedParams)
    }

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
            it.eval(stack, context)
        }

        context.exitScope()
    }
}

data class Signature(
    val typeSignature: Map<String, Type>,
    val params: Map<String, Type>,
    val returned: Type
) {
    fun matches(other: Signature): Boolean {
        if (!other.returned.accepts(this.returned)) {
            return false
        }

        if (params.size != other.params.size) {
            return false
        }
        val thisParams = params.values.toList()
        val otherParams = other.params.values.toList()

        return thisParams.withIndex().all {
            it.value.accepts(otherParams[it.index])
        }
    }
}
