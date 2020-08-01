package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandAccess
import me.bristermitten.frigga.runtime.command.CommandPropertyReference
import me.bristermitten.frigga.runtime.command.CommandReferencedCall

object ReferencedCallTransformer : NodeTransformer<FriggaParser.ReferencedCallExpressionContext>()
{

    override fun transformNode(node: FriggaParser.ReferencedCallExpressionContext): Command
    {
        val referencedCall = node.referencedCall()

        val access = NodeTransformers.transform(referencedCall.propertyAccess())

        val calling = if (access.command is CommandAccess)
        {
            access.command.property
        } else
        {
            (access.command as CommandPropertyReference).referencing
        }

        val upon = (access.command as? CommandAccess)?.upon

        return CommandReferencedCall(
            upon,
            calling,
            referencedCall
                .refererencedCallParameters()
                .functionCallParametersList()
                .indexedFunctionCallParameter()
                .map { it.expression() }
                .map(NodeTransformers::transform)
        )
    }
}
