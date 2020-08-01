package me.bristermitten.frigga.transform

import FriggaParser.FunctionExpressionContext
import FriggaParser.PropertyAssignmentContext
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandFunctionValue
import me.bristermitten.frigga.runtime.data.function.Signature

object FunctionTransformer : NodeTransformer<FunctionExpressionContext>()
{

    override fun transformNode(node: FunctionExpressionContext): Command
    {
        var parent = node.parent
        while (parent !is PropertyAssignmentContext)
        {
            parent = parent.parent
            if (parent == null)
            {
                break
            }
        }

        val name = if (parent is PropertyAssignmentContext)
        {
            val assignedName =
                parent.typedPropertyDeclaration()?.ID()
                    ?: parent.untypedPropertyDeclaration()?.ID()
                    ?: throw IllegalArgumentException("How did we get here?")

            assignedName.text
        } else
        {
            "Anonymous"
        }

        return with(node.functionValue()) {
            val signatureContext = this.functionSignature()
            signatureContext.typeSignature() //TODO
            val params = signatureContext.functionArguments().functionArgument().map {
                it.ID().text to it.type().toType()
            }.toMap()
            val returnType = signatureContext.type().toType()

            val functionSignature = Signature(
                emptyMap(),
                params,
                returnType
            )

            val body = functionBody().body().transformBody()

            CommandFunctionValue(name, functionSignature, body)
        }
    }
}
