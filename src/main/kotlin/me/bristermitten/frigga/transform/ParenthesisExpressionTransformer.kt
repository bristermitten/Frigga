package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandParenthesisExpression

object ParenthesisExpressionTransformer : NodeTransformer<FriggaParser.ParenthesisedExpressionContext>() {

    override fun transformNode(node: FriggaParser.ParenthesisedExpressionContext): Command {
        return CommandParenthesisExpression(NodeTransformers.transform( node.expression()))
    }
}
