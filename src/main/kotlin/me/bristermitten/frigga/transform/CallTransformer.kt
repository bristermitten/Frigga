package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.*

object CallTransformer : NodeTransformer<FriggaParser.CallExpressionContext>()
{

    override fun transformNode(node: FriggaParser.CallExpressionContext): Command
    {
        val access = NodeTransformers.transform(node.expression())

        val command = access.command
        val calling = if (command is CommandAccess)
        {
            command.property
        }
        else
        {
            (command as CommandPropertyReference).referencing
        }

        val upon = (command as? CommandAccess)?.upon

        return CommandCall(
            upon,
            calling,
            node.functionCallParameters().functionCallParametersList().indexedFunctionCallParameter()
                .map { it.assignableExpression() }
                .map(NodeTransformers::transform))
    }

}
