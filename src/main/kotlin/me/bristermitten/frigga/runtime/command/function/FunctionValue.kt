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
        var index = 0
        signature.input.forEach { (paramName, paramType) ->
            val paramValue = paramValues[index]
            if (!paramType.accepts(paramValue.type)) {
                throw IllegalArgumentException("Cannot use ${paramValue.type} in place of $paramType for function $name")
            }
            context.defineProperty(paramName, paramValue)
            index++
        }
        if (callingUpon != null) {
            context.defineProperty(UPON_NAME, callingUpon, forceReservedName = true)
        }
        for (command in body) {
            try {
                command.eval(stack, context)
            } catch (e: BreakException) {
                if (name != "yield" && name != "break") {
                    break
                } else throw e
            }
        }
        context.leaveScope()
    }
}
