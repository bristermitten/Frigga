package me.bristermitten.frigga.runtime.command.function

import me.bristermitten.frigga.ast.element.BreakException
import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.Value
import me.bristermitten.frigga.runtime.command.Command

class CommandFunctionCall(
    val calling: String,
    val params: List<Command>
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        val callingUpon = stack.peek()
        if (callingUpon != null) {
            stack.pull()
        }

        val uponType = (callingUpon as? Value)?.type
        val paramValues = params.map {
            it.eval(stack, context)
            stack.pull() as Value
        }

        val function = context.findFunction(uponType, calling, paramValues.map(Value::type))

        requireNotNull(function) {
            "No such function $calling"
        }

        context.enterScope(calling)
        var index = 0
        function.signature.input.forEach { (paramName, paramType) ->
            val paramValue = paramValues[index]
            if (!paramType.accepts(paramValue.type)) {
                throw IllegalArgumentException("Cannot use ${paramValue.type} in place of $paramType for function $calling")
            }
            context.defineProperty(paramName, paramValue)
            index++
        }

        if (callingUpon != null) {
            context.defineProperty("__upon", callingUpon as Value)
        }

        for (command in function.body) {
            try {
                command.eval(stack, context)
            } catch (e: BreakException) {
                if (this.calling != "yield" && this.calling != "break") {
                    break
                } else throw e
            }
        }
        context.leaveScope()

    }

    override fun toString(): String {
        return "CommandFunctionCall(calling='$calling', params=$params)"
    }
}
