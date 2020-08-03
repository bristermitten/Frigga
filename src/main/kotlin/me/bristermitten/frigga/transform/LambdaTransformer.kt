package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandFunctionValue
import me.bristermitten.frigga.runtime.data.function.Signature
import me.bristermitten.frigga.runtime.type.AnyType
import me.bristermitten.frigga.runtime.type.NothingType


object LambdaTransformer : NodeTransformer<FriggaParser.LambdaExpressionContext>()
{

    override fun transformNode(node: FriggaParser.LambdaExpressionContext): Command
    {
        with(node.lambdaValue()) {
            when (this)
            {
                is FriggaParser.BlockLambdaContext ->
                {
                    val signature = Signature(emptyMap(), emptyMap(), NothingType)
                    val content = this.functionBody().body().transformBody()
                    return CommandFunctionValue("Anonymous", signature, content, null)
                }
                is FriggaParser.TypedSingleExpressionLambdaContext ->
                {
                    val params = this.functionArguments().functionArgument().map {
                        it.ID().text to it.type().toType()
                    }.toMap()

                    val signature = Signature(emptyMap(), params, NothingType)

                    val content = NodeTransformers.transform(this.assignableExpression())
                    return CommandFunctionValue("Anonymous", signature, listOf(content), null)
                }
                is FriggaParser.UntypedSingleExpressionLambdaContext -> {
                    val params = this.lambdaArguments().ID().map {
                        it.text to AnyType
                    }.toMap()

                    val signature = Signature(emptyMap(), params, NothingType)

                    val content = NodeTransformers.transform(this.assignableExpression())
                    return CommandFunctionValue("Anonymous", signature, listOf(content), null)
                }
            }
            throw UnsupportedOperationException("$text ${this.javaClass}")
        }
    }
}
