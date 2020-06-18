package me.bristermitten.frigga.ast.element.function

import me.bristermitten.frigga.ast.element.NothingType
import me.bristermitten.frigga.ast.element.Type
import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.function.FunctionValue

data class NamedFunctionValue(val name: String, val functionValue: FunctionValue)

inline fun function(closure: FunctionDefiner.() -> Unit): NamedFunctionValue {
    val definer = FunctionDefiner()
    definer.closure()
    return NamedFunctionValue(
        definer.name,
        FunctionValue(
            definer.signature,
            definer.body
        )
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

fun FunctionDefiner.body(body: List<Command>) {
    this.body = body
}

fun FunctionDefiner.body(single: Command) {
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

fun singleCommand(onProcess: (Stack, FriggaContext) -> Unit): Command = SingleCommand(onProcess)


class FunctionDefiner {
    var name: String = ""
    var signature: Signature = Signature()
    var body: List<Command> = emptyList()
}

class FunctionSignature {
    var typeSignature = emptyMap<String, Type>()
    var input = emptyMap<String, Type>()
    var output: Type = NothingType
}
