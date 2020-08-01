package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.THIS_NAME
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.data.function.Function
import me.bristermitten.frigga.runtime.type.TypeInstance

data class CommandCall(
    val upon: CommandNode?,
    val calling: String,
    val params: List<CommandNode>
) : Command() {

    private var function: Function? = null

    override fun eval(stack: Stack, context: FriggaContext) {

        val callingUpon = upon?.let {
            it.command.eval(stack, context)

            requireNotNull(stack.pull()) {
                "Expression $upon did not yield a value to receive a call."
            }
        }


        val paramValues = params.map {
            it.command.eval(stack, context)
            stack.pull()
        }

        if (function != null) {
            function?.call(callingUpon, stack, context, paramValues)
            return
        }

        val paramTypes = paramValues.map(Value::type)
        val uponType = callingUpon?.type

        val typeInstance = callingUpon?.value as? TypeInstance
        val function = if (typeInstance != null) {
            context.findTypeFunction(callingUpon.type, typeInstance, calling, paramTypes)
        } else {
            context.findFunction(uponType, calling, paramTypes)
        }

        this.function = function

        requireNotNull(function) {
            "No such function $uponType#$calling(${paramTypes.joinToString(prefix = "", postfix = "")})"
        }

        function.call(callingUpon, stack, context, paramValues)

    }
}
