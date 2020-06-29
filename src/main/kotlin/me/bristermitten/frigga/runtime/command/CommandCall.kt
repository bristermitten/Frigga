package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.data.Value

data class CommandCall(
    val upon: CommandNode?,
    val calling: String,
    val params: List<CommandNode>
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {

        val callingUpon = upon?.let {
            it.command.eval(stack, context)

            requireNotNull(stack.pull()) {
                "Expression $upon did not yield a value to receive a call."
            }
        }

        val uponType = callingUpon?.type

        val paramValues = params.map {
            it.command.eval(stack, context)
            stack.pull()
        }

        val function = context.findFunction(uponType, calling, paramValues.map(Value::type))

        requireNotNull(function) {
            "No such function $uponType#$calling"
        }

        function.call(stack, context, paramValues)

    }
}
