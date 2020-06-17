package me.bristermitten.frigga.runtime.command.function

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.Value
import me.bristermitten.frigga.runtime.command.Command

class CommandFunctionCall(
    val calling: String,
    val params: List<Command>
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        val callingUpon = stack.peek() as Value?
        if (callingUpon != null) {
            stack.pull()
        }

        val uponType = callingUpon?.type
        val paramValues = params.map {
            it.eval(stack, context)
            stack.pull() as Value
        }

        val function = context.findFunction(uponType, calling, paramValues.map(Value::type))

        requireNotNull(function) {
            "No such function $calling"
        }

        function.call(calling, callingUpon,  stack, context, paramValues)
    }

    override fun toString(): String {
        return "CommandFunctionCall(calling='$calling', params=$params)"
    }
}
