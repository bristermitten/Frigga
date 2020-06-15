package me.bristermitten.frigga.scope

import me.bristermitten.frigga.ast.element.FriggaFile
import me.bristermitten.frigga.ast.element.expression.Expression
import me.bristermitten.frigga.ast.element.expression.value.Assignment
import me.bristermitten.frigga.ast.element.expression.value.BinaryOperator
import me.bristermitten.frigga.ast.element.expression.value.Literal
import me.bristermitten.frigga.ast.element.expression.value.PropertyReference
import me.bristermitten.frigga.runtime.*
import me.bristermitten.frigga.runtime.operator.CommandBinaryAdd

class FriggaRuntime {
    private val context = FriggaContext()

    internal fun process(file: FriggaFile) {
        file.contents.forEach {
            val command = process(it)
            command.eval(context.stack, context)
        }
        val leftover = context.stack.peek()
        if (leftover != null) {
            val toPrint = (leftover as? Value)?.value ?: leftover
            println(toPrint)
        }
    }

    internal fun process(expression: Expression): Command {
        if (expression is Assignment) {
            val defineCommand = CommandPropertyDefine(
                expression.assignTo,
                expression
            )
            val value = process(expression.value)
            value.eval(context.stack, context)

            return defineCommand
        }
        if (expression is Literal<*>) {
            return CommandLiteral(
                Value(expression.type, expression.value)
            )
        }
        if (expression is PropertyReference) {
            return CommandPropertyReference(expression.referencing)
        }
        if (expression is BinaryOperator) {
            //TODO more operators
            return CommandBinaryAdd(process(expression.left), process(expression.right))
        }
        throw UnsupportedOperationException(expression.javaClass.simpleName + " " + expression)
    }
}
