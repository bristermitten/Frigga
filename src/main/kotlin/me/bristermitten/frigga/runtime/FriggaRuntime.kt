package me.bristermitten.frigga.runtime

import FriggaLexer
import FriggaParser
import getJVMType
import loadTypes
import me.bristermitten.frigga.runtime.data.FriggaFile
import me.bristermitten.frigga.runtime.data.JVMNamespace
import me.bristermitten.frigga.runtime.error.ExecutionException
import me.bristermitten.frigga.transform.load
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.atn.PredictionMode
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class FriggaRuntime {
    private val namespaces = mutableMapOf<String, FriggaContext>()
    private val globalContext = FriggaContext("")


    init {
        namespaces[""] = globalContext
        loadTypes()
        loadStdLib()
    }

    private fun loadStdLib() {
        val resource =
            javaClass.classLoader.getResource("std") ?: throw NoSuchElementException("Could not get std folder.")
        File(resource.toURI()).listFiles()?.forEach {
            execute(it.readText())
        }
    }

    @OptIn(ExperimentalTime::class)
    fun execute(friggaCode: String): FullExecutionResult {

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
        globalContext.reset()
    }

    private fun getNamespace(namespace: String) = namespaces.getOrPut(namespace) { FriggaContext(namespace) }

    private fun process(file: FriggaFile): ExecutionResult {
        val exceptions = mutableListOf<Exception>()
        val namespace = file.namespace

        val context = if (namespace != null) {
            getNamespace(namespace.name)
        } else {
            this.globalContext
        }

        file.using.forEach {
            if (it is JVMNamespace) {
                context.defineType(getJVMType(it.jvmClass))
            } else {
                context.use(getNamespace(it.name))
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
