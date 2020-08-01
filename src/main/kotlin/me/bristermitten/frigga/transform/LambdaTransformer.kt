package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandFunctionValue
import me.bristermitten.frigga.runtime.data.function.Signature
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
                    return CommandFunctionValue("Anonymous", signature, content)
                }
                is FriggaParser.TypedSingleExpressionLambdaContext ->
                {
                    val params = this.functionArguments().functionArgument().map {
                        it.ID().text to it.type().toType()
                    }.toMap()

                    val signature = Signature(emptyMap(), params, NothingType)

                    val content = NodeTransformers.transform(this.expression())
                    return CommandFunctionValue("Anonymous", signature, listOf(content))
                }
            }
            throw UnsupportedOperationException(text)
//            val params = this.lambdaParams()?.lamdaParam()
//                ?.map {
//                    val functionParam = it.functionParam()
//                    if (functionParam == null)
//                    {
//                        it.ID().text to AnyType
//                    } else
//                    {
//                        functionParam.ID().text to functionParam.typeSpec().type().toType()
//                    }
//                }?.toMap() ?: emptyMap()
//
//            val block = block()?.body()?.line()?.map(FriggaParser.LineContext::expression) ?: listOf(expression())
//
//            val content = block
//                .map(NodeTransformers::transform)
//
//            val signature = Signature(
//                emptyMap(),
//                params,
//                AnyType
//            )
//
//            return CommandFunctionValue("Anonymous", signature, content)
        }
    }
}
