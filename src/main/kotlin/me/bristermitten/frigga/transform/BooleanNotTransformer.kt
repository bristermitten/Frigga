package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandBooleanNot

object BooleanNotTransformer : NodeTransformer<FriggaParser.BooleanNotContext>() {

    override fun transformNode(node: FriggaParser.BooleanNotContext): Command {

        return CommandBooleanNot(NodeTransformers.transform(node.inverse().expression()).command)
    }
}
