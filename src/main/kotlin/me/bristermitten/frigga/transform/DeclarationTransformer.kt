package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandDeclaration
import me.bristermitten.frigga.runtime.data.PropertyDeclaration
import me.bristermitten.frigga.transform.AssignmentTransformer.toModifier

object DeclarationTransformer : NodeTransformer<FriggaParser.DeclarationExpressionContext>() {

    override fun transformNode(node: FriggaParser.DeclarationExpressionContext): Command {
        val full = node.fullDeclaration()
        return CommandDeclaration(
            PropertyDeclaration(
                full.propertyModifier().map { it.toModifier() }.toSet(),
                full.ID().text,
                full.typeSpec().type().toType()
            )
        )
    }
}
