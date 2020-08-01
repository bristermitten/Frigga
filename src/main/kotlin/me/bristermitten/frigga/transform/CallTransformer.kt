package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandAccess
import me.bristermitten.frigga.runtime.command.CommandCall
import me.bristermitten.frigga.runtime.command.CommandPropertyReference

object CallTransformer : NodeTransformer<FriggaParser.FunctionCallContext>()
{

    override fun transformNode(node: FriggaParser.FunctionCallContext): Command
    {
        val access = NodeTransformers.transform(node.propertyAccess())

        val calling = if (access.command is CommandAccess)
        {
            access.command.property
        } else
        {
            (access.command as CommandPropertyReference).referencing
        }

        val upon = (access.command as? CommandAccess)?.upon

        return CommandCall(
            upon,
            calling,
            node.functionCallParameters().functionCallParametersList().indexedFunctionCallParameter()
                .map { it.expression() }
                .map(NodeTransformers::transform))
    }

}
