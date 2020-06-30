package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandAssignment
import me.bristermitten.frigga.runtime.data.Modifier

object AssignmentTransformer : NodeTransformer<FriggaParser.AssignmentExpressionContext>() {

    override fun transformNode(node: FriggaParser.AssignmentExpressionContext): Command {
        with(node.assignment()) {
            val declaration = this.declaration()
            val modifiers = declaration.propertyModifier()
                .mapNotNull {
                    it.toModifier()
                }.toSet()


            val name = declaration.ID().text

            val expression = this.expression()

            val value = NodeTransformers.transform(expression)

            return CommandAssignment(name, modifiers, value)
        }
    }

    fun FriggaParser.PropertyModifierContext.toModifier(): Modifier {
        if (MUTABLE() != null) {
            return Modifier.MUTABLE
        }
        if (NATIVE() != null) {
            return Modifier.NATIVE
        }

        throw UnsupportedOperationException("No such modifier $text")
    }
}
