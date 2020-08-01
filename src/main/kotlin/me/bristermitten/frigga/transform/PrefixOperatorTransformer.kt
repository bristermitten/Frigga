package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandBooleanNot
import me.bristermitten.frigga.runtime.command.prefixOperatorFromSymbol

object PrefixOperatorTransformer : NodeTransformer<FriggaParser.PrefixOperatorExpressionContext>()
{

    override fun transformNode(node: FriggaParser.PrefixOperatorExpressionContext): Command
    {
        val prefixOperatorCall = node.prefixOperatorCall()
        val operation = prefixOperatorFromSymbol(prefixOperatorCall.operator.text)

        requireNotNull(operation) {
            "No such operator ${prefixOperatorCall.operator}"
        }

        //TODO binary not and extendable pls
        return CommandBooleanNot(NodeTransformers.transform(prefixOperatorCall.expression()).command)
    }
}
