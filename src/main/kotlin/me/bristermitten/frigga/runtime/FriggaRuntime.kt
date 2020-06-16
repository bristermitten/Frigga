package me.bristermitten.frigga.runtime

import FriggaLexer
import FriggaParser
import me.bristermitten.frigga.ast.element.AnyType
import me.bristermitten.frigga.ast.element.FriggaFile
import me.bristermitten.frigga.ast.element.expression.Expression
import me.bristermitten.frigga.ast.element.expression.value.*
import me.bristermitten.frigga.ast.element.function.Function
import me.bristermitten.frigga.ast.element.function.Signature
import me.bristermitten.frigga.ast.toAST
import me.bristermitten.frigga.runtime.*
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandLiteral
import me.bristermitten.frigga.runtime.command.CommandPropertyDefine
import me.bristermitten.frigga.runtime.command.CommandPropertyReference
import me.bristermitten.frigga.runtime.command.function.CommandFunctionCall
import me.bristermitten.frigga.runtime.command.function.CommandFunctionDefinition
import me.bristermitten.frigga.runtime.command.operator.CommandBinaryAdd
import me.bristermitten.frigga.runtime.command.operator.CommandBinarySubtract
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.atn.PredictionMode
import java.io.File
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class FriggaRuntime {
    private val context = FriggaContext()

    fun loadStdLib() {
        val resource =
            javaClass.classLoader.getResource("std") ?: throw NoSuchElementException("Could not get std folder.")
        File(resource.toURI()).listFiles()?.forEach {
            execute(it.readText(), it.name)
        }
    }

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
        return when (expression) {
            is Assignment -> {
                val defineCommand = CommandPropertyDefine(
                    expression.assignTo,
                    expression
                )
                val value = process(expression.value)
                value.eval(context.stack, context)

                defineCommand
            }
            is Literal<*> -> {
                CommandLiteral(
                    Value(expression.type, expression.value)
                )
            }
            is PropertyReference -> {
                CommandPropertyReference(expression.referencing)
            }
            is BinaryOperator -> {
                when (expression.operator) {
                    "+" -> CommandBinaryAdd(process(expression.left), process(expression.right))
                    "-" -> CommandBinarySubtract(process(expression.left), process(expression.right))
                    else -> throw UnsupportedOperationException(expression.operator)
                }
            }
            is Lambda -> {
                CommandFunctionDefinition(
                    Signature(
                        emptyMap(),
                        expression.params.mapNotNull { it.key to (it.value.type ?: return@mapNotNull null) }.toMap(),
                        AnyType //TODO
                    ),
                    expression.body.map { process(it) }
                )
            }
            is Call -> {
                CommandFunctionCall(
                    expression.calling, expression.args.map(this::process)
                )
            }
            is Function -> {
                CommandFunctionDefinition(
                    expression.signature,
                    expression.contents.map { process(it) }
                )
            }
            else -> throw UnsupportedOperationException(expression.javaClass.simpleName + " " + expression)
        }
    }
}
