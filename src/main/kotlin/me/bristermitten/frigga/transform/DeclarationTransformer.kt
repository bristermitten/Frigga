package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandDeclaration
import me.bristermitten.frigga.runtime.data.PropertyDeclaration
import me.bristermitten.frigga.transform.AssignmentTransformer.toModifier

object DeclarationTransformer : NodeTransformer<FriggaParser.PropertyDeclarationStatementContext>()
{

    override fun transformNode(node: FriggaParser.PropertyDeclarationStatementContext): Command
    {
        with(node.propertyDeclaration()) {
            val full = this.typedPropertyDeclaration()
            return CommandDeclaration(
                PropertyDeclaration(
                    full.propertyModifier().map { it.toModifier() }.toSet(),
                    full.ID().text,
                    full.type().toType()
                )
            )
        }
    }
}
