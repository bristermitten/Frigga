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
                    val mutable = it.MUTABLE()
                    mutable?.let {
                        Modifier.MUTABLE
                    }
                }.toSet()


            val name = declaration.ID().text

            val expression = this.expression()

            val value = NodeTransformers.transform(expression)

            return CommandAssignment(name, modifiers, value)
        }
    }
}
