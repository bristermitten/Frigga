package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandAccess

object AccessTransformer : NodeTransformer<FriggaParser.AccessExpressionContext>()
{

    override fun transformNode(node: FriggaParser.AccessExpressionContext): Command
    {
        val upon = NodeTransformers.transform(node.expression())
        val property = node.ID().text
        return CommandAccess(upon, property)
    }
}
