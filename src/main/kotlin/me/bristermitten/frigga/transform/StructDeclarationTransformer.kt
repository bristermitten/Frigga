package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandStructureDefinition
import me.bristermitten.frigga.runtime.data.structure.Struct
import me.bristermitten.frigga.runtime.type.AnyType
import me.bristermitten.frigga.runtime.type.TypeProperty

object StructDeclarationTransformer : NodeTransformer<FriggaParser.StructDeclarationContext>()
{

    override fun transformNode(node: FriggaParser.StructDeclarationContext): Command
    {
        return CommandStructureDefinition(
            Struct(
                node.ID().text,
                emptyList(),
                node.structBody().statement()
                    .map {
                        if (it is FriggaParser.PropertyDeclarationStatementContext)
                        {
                            val propertyDeclaration = it.propertyDeclaration().typedPropertyDeclaration()
                            TypeProperty(
                                propertyDeclaration.ID().text, propertyDeclaration.type().toType(),
                                null
                            )
                        } else if (it is FriggaParser.PropertyAssignmentStatementContext)
                        {
                            val assignment = it.propertyAssignment()
                            val typed = assignment.typedPropertyDeclaration()
                            if (typed != null)
                            {
                                TypeProperty(typed.ID().text, typed.type().toType(), null)
                            }
                            val untyped = assignment.untypedPropertyDeclaration()
                            if (untyped != null)
                            {
                                TypeProperty(untyped.ID().text, AnyType, null)
                            } else
                            {
                                throw IllegalArgumentException("Illegal statement inside struct ${it.text}")
                            }

                        } else
                        {
                            throw IllegalArgumentException("Illegal statement inside struct ${it.text}")
                        }
                    })
        )
    }
}
