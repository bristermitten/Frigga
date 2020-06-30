package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandAccess
import me.bristermitten.frigga.runtime.command.CommandReferencedCall

object ReferencedCallTransformer : NodeTransformer<FriggaParser.ReferencedCallExpressionContext>() {

    override fun transformNode(node: FriggaParser.ReferencedCallExpressionContext): Command {
        val expression = node.expression()
        val access = NodeTransformers.transform(expression).command as? CommandAccess
        val calling = access?.property ?: expression.text

        return CommandReferencedCall(
            access?.upon,
            calling,
            node.referencedCall().args().expression().map(NodeTransformers::transform)
        )
    }
}
