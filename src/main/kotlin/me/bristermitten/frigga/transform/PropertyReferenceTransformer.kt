package me.bristermitten.frigga.transform

import FriggaParser.PropertyReferenceContext
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandPropertyReference

object PropertyReferenceTransformer : NodeTransformer<PropertyReferenceContext>() {

    override fun transformNode(node: PropertyReferenceContext): Command {
        return CommandPropertyReference(node.text)
    }
}
