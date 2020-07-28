package me.bristermitten.frigga.transform

import FriggaParser
import FriggaParser.LineContext
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandFunctionValue
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.data.function.Signature
import me.bristermitten.frigga.runtime.type.AnyType

object LambdaTransformer : NodeTransformer<FriggaParser.LambdaExpressionContext>() {

    override fun transformNode(node: FriggaParser.LambdaExpressionContext): Command {

        with(node.lambda()) {
            val params = this.lambdaParams()?.lamdaParam()
                ?.map {
                    val functionParam = it.functionParam()
                    if (functionParam == null) {
                        it.ID().text to AnyType
                    } else {
                        functionParam.ID().text to functionParam.typeSpec().type().toType()
                    }
                }?.toMap() ?: emptyMap()

            val block = block()?.body()?.line()?.map(LineContext::expression) ?: listOf(expression())

            val content = block
                .map(NodeTransformers::transform)

            val signature = Signature(
                emptyMap(),
                params,
                AnyType
            )

            return CommandFunctionValue("Anonymous", signature, content)
        }
    }
}
