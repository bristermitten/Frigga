package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandTraitDefinition
import me.bristermitten.frigga.runtime.data.PropertyDeclaration
import me.bristermitten.frigga.transform.DeclarationTransformer.toModifier

object TraitDeclarationTransformer : NodeTransformer<FriggaParser.TraitDeclarationContext>()
{

    override fun transformNode(node: FriggaParser.TraitDeclarationContext): Command
    {
        return CommandTraitDefinition(
            node.ID().text,
            emptyList(),
            node.structBody().statement()
                .filterIsInstance<FriggaParser.PropertyDeclarationStatementContext>()
                .map { line ->
                    val fullDeclaration = line
                        .propertyDeclaration()
                        .typedPropertyDeclaration()

                    PropertyDeclaration(
                        fullDeclaration.propertyModifier().map { it.toModifier() }.toSet(),
                        fullDeclaration.ID().text,
                        fullDeclaration.propertyType().type().toType()
                    )
                })
    }
}
