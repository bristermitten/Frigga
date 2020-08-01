package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandAccess
import me.bristermitten.frigga.runtime.command.CommandPropertyReference

object PropertyAccessTransformer : NodeTransformer<FriggaParser.PropertyAccessContext>()
{

    override fun transformNode(node: FriggaParser.PropertyAccessContext): Command
    {
        if (node is FriggaParser.DirectAccessContext)
        {
            return CommandPropertyReference(node.ID().text)
        }

        if (node is FriggaParser.ChildAccessContext)
        {
            val upon = NodeTransformers.transform(node.propertyAccess())
            val property = node.ID().text
            return CommandAccess(upon, property)
        }

        throw UnsupportedOperationException(node.text)
    }
}
