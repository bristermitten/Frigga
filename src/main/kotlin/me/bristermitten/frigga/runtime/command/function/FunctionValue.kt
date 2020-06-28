package me.bristermitten.frigga.runtime.command.function

import me.bristermitten.frigga.ast.element.function.Signature
import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.UPON_NAME
import me.bristermitten.frigga.runtime.Value
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.error.BreakException

data class FunctionValue(
    val signature: Signature,
    val body: List<Command>
) {
    fun call(name: String, callingUpon: Value?, stack: Stack, context: FriggaContext, paramValues: List<Value>) {
        context.enterScope(name)


        signature.input.entries.withIndex().forEach { (index, param) ->
            val parameterName = param.key
            val parameterType = param.value
            val providedValue = paramValues[index]
            if (!parameterType.accepts(providedValue.type)) {
                throw IllegalArgumentException("Cannot use ${providedValue.type} in place of $parameterType for function $name")
            }
            context.defineProperty(parameterName, providedValue)
        }

        if (callingUpon != null) {
            context.defineProperty(UPON_NAME, callingUpon, forceReservedName = true)
        }

        for (command in body) {
            try {
                command.eval(stack, context)
            } catch (e: BreakException) {
                if (name != "yield" && name != "break") { //TODO replace with something a bit more extendable. annotations perhaps?
                    break
                } else {
                    throw e
                }
            }
        }

        context.leaveScope()
    }
}
