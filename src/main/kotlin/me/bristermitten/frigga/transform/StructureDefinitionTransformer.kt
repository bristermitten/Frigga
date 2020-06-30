package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandStructureDefinition
import me.bristermitten.frigga.runtime.data.PropertyDeclaration
import me.bristermitten.frigga.runtime.data.structure.Struct
import me.bristermitten.frigga.runtime.data.structure.Trait
import me.bristermitten.frigga.runtime.type.TypeProperty
import me.bristermitten.frigga.transform.AssignmentTransformer.toModifier

object StructureDefinitionTransformer : NodeTransformer<FriggaParser.StructureDefContext>() {

    override fun transformNode(node: FriggaParser.StructureDefContext): Command {
        return CommandStructureDefinition(
            when (node) {
                is FriggaParser.TraitDefinitionContext -> {
                    Trait(
                        node.traitDef().ID().text,
                        emptyList(),
                        node.traitDef().structureBody().structureLine()
                            .filterIsInstance<FriggaParser.DeclarationLineContext>()
                            .map { line ->
                                val fullDeclaration = line.fullDeclaration()
                                PropertyDeclaration(
                                    fullDeclaration.propertyModifier().map { it.toModifier() }.toSet(),
                                    fullDeclaration.ID().text,
                                    fullDeclaration.typeSpec().type().toType()
                                )
                            })
                }
                is FriggaParser.StructDefinitionContext -> {
                    Struct(
                        node.structDef().ID().text,
                        emptyList(),
                        node.structDef().structureBody().structureLine()
                            .filterIsInstance<FriggaParser.DeclarationLineContext>()
                            .map {
                                TypeProperty(
                                    it.fullDeclaration().ID().text, it.fullDeclaration().typeSpec().type().toType(),
                                    null
                                )
                            })
                }
                else -> throw UnsupportedOperationException(node.javaClass.name)
            }
        )
    }
}
