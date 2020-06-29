package me.bristermitten.frigga.transform

import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.data.Position
import org.antlr.v4.runtime.ParserRuleContext

abstract class NodeTransformer<F : ParserRuleContext> {
    fun transform(transform: F): CommandNode {

        fun Command.toNode(node: F): CommandNode {
            return CommandNode(
                this,
                Position(
                    node.start.line, node.start.charPositionInLine
                )
            )
        }

        return transformNode(transform).toNode(transform)
    }

    internal abstract fun transformNode(node: F): Command

}

fun <F : ParserRuleContext, R : ParserRuleContext> delegateTransformer(
    other: NodeTransformer<R>,
    transformer: (F) -> R
): NodeTransformer<F> {
    return object : NodeTransformer<F>() {
        override fun transformNode(node: F): Command {

            val transform = transformer(node)
            return other.transformNode(transform)
        }

    }
}
