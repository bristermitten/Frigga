package me.bristermitten.frigga.scope

import me.bristermitten.frigga.ast.element.FriggaFile
import me.bristermitten.frigga.ast.element.expression.Expression
import me.bristermitten.frigga.ast.element.expression.value.Assignment
import me.bristermitten.frigga.ast.element.expression.value.Literal
import me.bristermitten.frigga.runtime.Command
import me.bristermitten.frigga.runtime.CommandLiteral
import me.bristermitten.frigga.runtime.CommandPropertyDefine
import kotlin.math.exp

class FriggaRuntime {
    private val context = FriggaContext()

    internal fun process(file: FriggaFile) {
        file.contents.forEach {
            val command = process(it)
            command.eval(context.stack, context)
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
            return CommandLiteral(expression)
        }
        throw UnsupportedOperationException(expression.javaClass.simpleName + " " + expression)
    }
}
