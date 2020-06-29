package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command

object ParenthesisExpressionTransformer : NodeTransformer<FriggaParser.ParenthesisExpressionContext>() {

    override fun transformNode(node: FriggaParser.ParenthesisExpressionContext): Command {
        return NodeTransformers.transform(node.parenthesizedExpression().expression()).command
    }
}
