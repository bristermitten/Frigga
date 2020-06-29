package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandAssignment
import me.bristermitten.frigga.runtime.data.Modifier

object AssignmentTransformer : NodeTransformer<FriggaParser.AssignmentExpressionContext>() {

    override fun transformNode(node: FriggaParser.AssignmentExpressionContext): Command {
        with(node.assignment()) {

            val modifiers = this.propertyModifier()
                .mapNotNull {
                    val mutable = it.MUTABLE()
                    mutable?.let {
                        Modifier.MUTABLE
                    }
                }.toSet()

            val name = this.ID().text

            val expression = this.expression()
            val value = NodeTransformers.transform(expression)

            return CommandAssignment(name, modifiers, value)
        }
    }
}
