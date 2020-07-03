package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandParenthesisExpression

object ParenthesisExpressionTransformer : NodeTransformer<FriggaParser.ParenthesisExpressionContext>() {

    override fun transformNode(node: FriggaParser.ParenthesisExpressionContext): Command {
        return CommandParenthesisExpression(NodeTransformers.transform( node.parenthesizedExpression().expression()))
    }
}
