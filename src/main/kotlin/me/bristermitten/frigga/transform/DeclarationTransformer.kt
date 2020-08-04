package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandAssignment
import me.bristermitten.frigga.runtime.command.CommandDeclaration
import me.bristermitten.frigga.runtime.data.Modifier
import me.bristermitten.frigga.runtime.data.PropertyDeclaration
import me.bristermitten.frigga.runtime.type.AnyType
import me.bristermitten.frigga.runtime.type.Type

object DeclarationTransformer : NodeTransformer<FriggaParser.PropertyDeclarationContext>()
{

    override fun transformNode(node: FriggaParser.PropertyDeclarationContext): Command
    {
        with(node) {
            val typed = typedPropertyDeclaration()
            if (typed != null)
            {
                return CommandDeclaration(
                    PropertyDeclaration(
                        typed.propertyModifier()?.map { it.toModifier() }?.toSet() ?: emptySet(),
                        typed.ID().text,
                        typed.propertyType().type().toType()
                    )
                )
            }

            val untyped = untypedPropertyDeclaration()

            val name = untyped.ID().text

            val expression = assignableExpression()

            val value = NodeTransformers.transform(expression)
            val modifiers = untyped.propertyModifier().map { it.toModifier() }.toSet()

            return CommandAssignment(null, name, modifiers, value, AnyType)
        }
    }

    fun FriggaParser.PropertyModifierContext.toModifier(): Modifier
    {
        if (MUTABLE() != null)
        {
            return Modifier.MUTABLE
        }
        if (NATIVE() != null)
        {
            return Modifier.NATIVE
        }

        throw UnsupportedOperationException("No such modifier $text")
    }
}
