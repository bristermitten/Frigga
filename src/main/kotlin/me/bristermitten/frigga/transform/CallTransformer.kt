package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandAccess
import me.bristermitten.frigga.runtime.command.CommandCall

object CallTransformer : NodeTransformer<FriggaParser.CallExpressionContext>() {

    override fun transformNode(node: FriggaParser.CallExpressionContext): Command {
        val expression = node.callableExpression()
        val access = NodeTransformers.transform(expression).command as? CommandAccess
        val calling = access?.property ?: expression.text

        return CommandCall(access?.upon, calling, node.call().args().expression().map(NodeTransformers::transform))
    }

}
