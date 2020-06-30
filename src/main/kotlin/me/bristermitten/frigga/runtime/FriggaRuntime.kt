package me.bristermitten.frigga.runtime

import FriggaLexer
import FriggaParser
import getJVMType
import loadTypes
import me.bristermitten.frigga.runtime.data.FriggaFile
import me.bristermitten.frigga.runtime.data.JVMNamespace
import me.bristermitten.frigga.runtime.type.JVMType
import me.bristermitten.frigga.runtime.error.ExecutionException
import me.bristermitten.frigga.transform.load
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.atn.PredictionMode
import java.io.File
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
            friggaFile.load()
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
                context.defineType(getJVMType(it.jvmClass))
            }
        }
        file.content.forEach {
            try {
                it.command.eval(context.stack, context)
            } catch (e: Exception) {
                exceptions += ExecutionException(e, it.position)
            }
        }
        return ExecutionResult(
            context.stack.toList(),
            exceptions
        )
    }

}
