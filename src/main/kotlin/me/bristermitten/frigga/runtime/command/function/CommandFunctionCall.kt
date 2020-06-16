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
        val function = context.findFunction(uponType, calling)

        requireNotNull(function) {
            "No such function $calling"
        }

        params.forEach {
            it.eval(stack, context)
        }
        context.enterScope(calling)
        function.signature.input.forEach { (paramName, paramType) ->
            val paramValue = stack.pull() as Value
            if (!paramType.accepts(paramValue.type)) {
                throw IllegalArgumentException("Cannot use ${paramValue.type} in place of $paramType for function $calling")
            }
            context.defineProperty(paramName, paramValue)
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
