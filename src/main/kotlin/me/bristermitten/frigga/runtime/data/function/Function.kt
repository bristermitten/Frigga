package me.bristermitten.frigga.runtime.data.function

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.error.BreakException
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
        context.enterFunctionScope(name)

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

        for (it in content) {
            try {
                it.eval(stack, context)
            } catch (breakException: BreakException) {
                if (name != "yield" && name != "break") { //TODO replace with something a bit more extendable. annotations perhaps?
                    break
                }
                throw breakException
            }
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

    fun typesMatch(others: List<Type>): Boolean {
        val thisParams = params.values.toList()

        if (this.params.isEmpty() && others.isEmpty()) {
            return true
        }
        if (thisParams.size != others.size) {
            return false
        }

        return thisParams.withIndex().all {
            it.value.accepts(others[it.index])
        }
    }
}
