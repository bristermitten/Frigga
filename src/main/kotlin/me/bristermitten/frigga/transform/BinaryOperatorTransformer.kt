package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandInfixFunction
import me.bristermitten.frigga.runtime.command.operatorFromSymbol

object BinaryOperatorTransformer : NodeTransformer<FriggaParser.BinaryOperatorExpressionContext>() {
    override fun transformNode(node: FriggaParser.BinaryOperatorExpressionContext): Command {

        val left = NodeTransformers.transform(node.left)
        val right = NodeTransformers.transform(node.right)
        val operator = node.operator.text

        return CommandInfixFunction(
            left, right,
            operatorFromSymbol(operator) ?: throw UnsupportedOperationException("No such operator $operator")
        )
    }
}
