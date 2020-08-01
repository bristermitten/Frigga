package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.data.function.Function
import me.bristermitten.frigga.runtime.data.function.Signature
import me.bristermitten.frigga.runtime.type.FunctionType

data class CommandFunctionValue(
    val name: String,
    val signature: Signature,
    val content: List<CommandNode>
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        val function = Function(name, signature, content)

        function.init(context)
        stack.push(Value(FunctionType(signature), function))
    }
}
