package me.bristermitten.frigga.transform

import FriggaParser
import FriggaParser.LineContext
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandValue
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.data.function.Function
import me.bristermitten.frigga.runtime.data.function.Signature
import me.bristermitten.frigga.runtime.type.AnyType
import me.bristermitten.frigga.runtime.type.FunctionType

object LambdaTransformer : NodeTransformer<FriggaParser.LambdaExpressionContext>() {

    override fun transformNode(node: FriggaParser.LambdaExpressionContext): Command {
        with(node.lambda()) {
            val block = block()
            if (block != null) {
                val content = block().body().line()
                    .map(LineContext::expression)
                    .map(NodeTransformers::transform)
                    .map(CommandNode::command)
                val signature = Signature(
                    emptyMap(),
                    emptyMap(),
                    AnyType
                )
                val function = Function("Anonymous", signature, content)
                return CommandValue(Value(FunctionType(signature), function))
            }
        }
        throw UnsupportedOperationException(node.text)
    }
}
