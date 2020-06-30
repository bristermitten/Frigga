package me.bristermitten.frigga.transform

import FriggaParser.*
import getType
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandValue
import me.bristermitten.frigga.runtime.data.CommandNode
import me.bristermitten.frigga.runtime.data.function.Function
import me.bristermitten.frigga.runtime.data.function.Signature
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.type.FunctionType

object FunctionTransformer : NodeTransformer<FunctionExpressionContext>() {

    override fun transformNode(node: FunctionExpressionContext): Command {
        val parent = node.parent
        val name: String
        name = if (parent is AssignmentContext) {
            parent.declaration().ID().text
        } else {
            "Anonymous"
        }

        return with(node.function()) {
            this.generic() //TODO
            val signature = this.functionSignature()
            val params = signature.functionParams().functionParam().map {
                it.ID().text to getType(
                    it.typeSpec().type().text
                )
            }.toMap()
            val returnType = getType(signature.type().text)

            val functionSignature = Signature(
                emptyMap(),
                params,
                returnType
            )

            val body = this.block().body().line()
                .map(LineContext::expression)
                .map(NodeTransformers::transform)
                .map(CommandNode::command)

            val function = Function(name, functionSignature, body)

            CommandValue(Value(FunctionType(functionSignature), function))
        }
    }
}
