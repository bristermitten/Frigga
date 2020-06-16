package me.bristermitten.frigga.scope

import FriggaLexer
import FriggaParser
import me.bristermitten.frigga.ast.element.FriggaFile
import me.bristermitten.frigga.ast.element.expression.Expression
import me.bristermitten.frigga.ast.element.expression.value.Assignment
import me.bristermitten.frigga.ast.element.expression.value.BinaryOperator
import me.bristermitten.frigga.ast.element.expression.value.Literal
import me.bristermitten.frigga.ast.element.expression.value.PropertyReference
import me.bristermitten.frigga.ast.toAST
import me.bristermitten.frigga.runtime.*
import me.bristermitten.frigga.runtime.operator.CommandBinaryAdd
import me.bristermitten.frigga.runtime.operator.CommandBinarySubtract
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.atn.PredictionMode
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class FriggaRuntime {
    private val context = FriggaContext()

    @OptIn(ExperimentalTime::class)
    fun execute(friggaCode: String, name: String): FullExecutionResult {
        val (lexer, lexTime) = measureTimedValue {
            FriggaLexer(CharStreams.fromString(friggaCode))
        }

        val stream = CommonTokenStream(lexer)

        val (friggaFile, parseTime) = measureTimedValue {
            val parser = FriggaParser(stream)
            parser.interpreter.predictionMode = PredictionMode.SLL
            try {
                parser.friggaFile()
            } catch (ex: Exception) {
                stream.seek(0)
                parser.reset()
                parser.interpreter.predictionMode = PredictionMode.LL
                parser.friggaFile()
            }
        }

        val (ast, runtimeLoadingTime) = measureTimedValue {
            friggaFile.toAST(name)
        }

        val (result, processTime) = measureTimedValue {
            process(ast)
        }

        return FullExecutionResult(
            result.leftoverStack,
            result.exceptions,
            Timings(
                lexTime.inMilliseconds,
                parseTime.inMilliseconds,
                runtimeLoadingTime.inMilliseconds,
                processTime.inMilliseconds
            )
        )
    }

    fun reset() = context.reset()

    private fun process(file: FriggaFile): ExecutionResult {
        val exceptions = mutableListOf<Exception>()
        file.contents.forEach {
            val command = process(it)
            try {
                command.eval(context.stack, context)
            } catch (e: Exception) {
                exceptions += e
            }
        }
        return ExecutionResult(
            context.stack.toList(),
            exceptions
        )
    }

    private fun process(expression: Expression): Command {
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
            return when (expression.operator) {
                "+" -> CommandBinaryAdd(process(expression.left), process(expression.right))
                "-" -> CommandBinarySubtract(process(expression.left), process(expression.right))
                else -> throw UnsupportedOperationException(expression.operator)
            }
        }
        throw UnsupportedOperationException(expression.javaClass.simpleName + " " + expression)
    }
}
