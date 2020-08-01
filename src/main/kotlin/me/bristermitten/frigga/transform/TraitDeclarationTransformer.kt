package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandStructureDefinition
import me.bristermitten.frigga.runtime.data.PropertyDeclaration
import me.bristermitten.frigga.runtime.data.structure.Trait
import me.bristermitten.frigga.transform.AssignmentTransformer.toModifier

object TraitDeclarationTransformer : NodeTransformer<FriggaParser.TraitDeclarationContext>()
{

    override fun transformNode(node: FriggaParser.TraitDeclarationContext): Command
    {
        return CommandStructureDefinition(
            Trait(
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
                            fullDeclaration.type().toType()
                        )
                    })
        )
    }
}
