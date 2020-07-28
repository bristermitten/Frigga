package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.data.function.Function
import me.bristermitten.frigga.runtime.type.FunctionType
import me.bristermitten.frigga.runtime.type.TypeInstance

data class CommandReferencedCall(
    val upon: CommandNode?,
    val calling: String,
    val params: List<CommandNode>
) : Command() {
    private var function: Function? = null

    private fun fetchFunction(stack: Stack, context: FriggaContext): Function {
        if (function != null) {
            return function!!
        }
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

        val paramTypes = paramValues.map(Value::type)
        val function = if (callingUpon?.value is TypeInstance) {
            context.findTypeFunction(callingUpon.type, callingUpon.value as TypeInstance, calling, paramTypes)
        } else {
            context.findFunction(uponType, calling, paramTypes)
        }

        this.function = function

        requireNotNull(function) {
            "No such function to reference $uponType#$calling($paramTypes)"
        }
        return function
    }

    override fun eval(stack: Stack, context: FriggaContext) {

        val function = fetchFunction(stack, context)

        val callFunction = Function(
            function.name, function.signature.copy(params = emptyMap()),
            listOf(CommandNode(CommandCall(upon, calling, params)))
        )

        stack.push(Value(FunctionType(callFunction.signature), callFunction))
    }
}
