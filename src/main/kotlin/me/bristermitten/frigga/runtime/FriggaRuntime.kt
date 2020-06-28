package me.bristermitten.frigga.runtime

import FriggaLexer
import FriggaParser
import me.bristermitten.frigga.ast.element.*
import me.bristermitten.frigga.ast.element.expression.Expression
import me.bristermitten.frigga.ast.element.expression.value.*
import me.bristermitten.frigga.ast.element.function.Function
import me.bristermitten.frigga.ast.element.function.Signature
import me.bristermitten.frigga.ast.toAST
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandPropertyDefine
import me.bristermitten.frigga.runtime.command.CommandPropertyReference
import me.bristermitten.frigga.runtime.command.function.CommandFunctionCall
import me.bristermitten.frigga.runtime.command.function.CommandFunctionDefinition
import me.bristermitten.frigga.runtime.command.operator.CommandBinaryOperator
import me.bristermitten.frigga.runtime.command.operator.operatorFromSymbol
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.atn.PredictionMode
import java.io.File
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class FriggaRuntime {
    private val context = FriggaContext()


    init {
        loadStdLib()
    }

    private fun loadStdLib() {
        val resource = javaClass.classLoader.getResource("std")
            ?: throw NoSuchElementException("Could not get std folder.")
        File(resource.toURI()).listFiles()?.forEach {
            execute(it.readText(), it.name)
        }
    }

    @OptIn(ExperimentalTime::class)
    fun execute(friggaCode: String, name: String): FullExecutionResult {
        loadTypes()
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

    fun reset() {
        context.reset()
        loadStdLib() //TODO maybe don't remove the whole stdlib?
    }

    private fun process(file: FriggaFile): ExecutionResult {
        val exceptions = mutableListOf<Exception>()
        file.using.forEach {
            if (it is JVMNamespace) {
                context.defineType(JVMType(it.jvmClass))
            }
        }
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
                    expression.modifiers,
                    process(expression.value)
                )

                defineCommand
            }
            is Literal<*> -> {
                expression
            }
            is PropertyReference -> {
                CommandPropertyReference(expression.referencing)
            }
            is BinaryOperator -> {
                return CommandBinaryOperator(
                    process(expression.left),
                    process(expression.right),
                    operatorFromSymbol(expression.operator)
                )
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
                    expression.upon?.let(this::process),
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
