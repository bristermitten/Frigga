package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandAssignment
import me.bristermitten.frigga.runtime.data.Modifier
import me.bristermitten.frigga.runtime.type.Type

object AssignmentTransformer : NodeTransformer<FriggaParser.PropertyAssignmentContext>()
{

    override fun transformNode(node: FriggaParser.PropertyAssignmentContext): Command
    {
        with(node) {
            val typed = typedPropertyDeclaration()

            val modifiers: Set<Modifier>
            val name: String
            val type: Type?
            if (typed != null)
            {
                modifiers = typed.propertyModifier()
                    .mapNotNull {
                        it.toModifier()
                    }.toSet()

                name = typed.ID().text
                type = typed.type().toType()
            } else
            {
                val untyped = untypedPropertyDeclaration()
                if (untyped != null)
                {
                    modifiers = untyped.propertyModifier()
                        .mapNotNull {
                            it.toModifier()
                        }.toSet()

                    name = untyped.ID().text
                    type = null
                } else
                {
                    throw UnsupportedOperationException(this.text)
                }
            }


            val expression = assignableExpression()

            val value = NodeTransformers.transform(expression)

            return CommandAssignment(name, modifiers, value, type)
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
