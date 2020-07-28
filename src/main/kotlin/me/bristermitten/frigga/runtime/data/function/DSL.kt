package me.bristermitten.frigga.runtime.data.function

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.type.NothingType
import me.bristermitten.frigga.runtime.type.Type


inline fun function(closure: FunctionDefiner.() -> Unit): Function {
    val definer = FunctionDefiner()
    definer.closure()
    return Function(
        definer.name,
        definer.signature,
        definer.body
    )
}

inline fun FunctionDefiner.signature(closure: FunctionSignature.() -> Unit) {
    val signature = FunctionSignature()
    signature.closure()
    closure(signature)
    this.signature = Signature(
        signature.typeSignature,
        signature.input,
        signature.output
    )
}

fun FunctionDefiner.body(body: List<CommandNode>) {
    this.body = body
}

fun FunctionDefiner.body(single: CommandNode) {
    this.body = listOf(single)
}

fun FunctionDefiner.body(onProcess: (Stack, FriggaContext) -> Unit) {
    this.body = listOf(singleCommand(onProcess))
}

private class SingleCommand(
    val onProcess: (Stack, FriggaContext) -> Unit
) : Command() {
    override fun eval(stack: Stack, context: FriggaContext) {
        onProcess(stack, context)
    }
}

fun singleCommand(onProcess: (Stack, FriggaContext) -> Unit): CommandNode = CommandNode(
    SingleCommand(onProcess)
)


class FunctionDefiner {
    var name: String = ""
    var signature: Signature = Signature(
        emptyMap(), emptyMap(), NothingType
    )
    var body: List<CommandNode> = emptyList()
}

class FunctionSignature {
    var typeSignature = emptyMap<String, Type>()
    var input = emptyMap<String, Type>()
    var output: Type = NothingType
}
