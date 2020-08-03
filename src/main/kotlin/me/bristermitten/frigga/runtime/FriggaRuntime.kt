package me.bristermitten.frigga.runtime

import BoolType
import FriggaLexer
import FriggaParser
import getJVMType
import loadTypes
import me.bristermitten.frigga.runtime.data.FriggaFile
import me.bristermitten.frigga.runtime.data.JVMNamespace
import me.bristermitten.frigga.runtime.data.Position
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.data.function.Function
import me.bristermitten.frigga.runtime.data.function.Signature
import me.bristermitten.frigga.runtime.data.function.singleCommand
import me.bristermitten.frigga.runtime.error.ExecutionException
import me.bristermitten.frigga.runtime.parser.ThrowingErrorListener
import me.bristermitten.frigga.runtime.type.AnyType
import me.bristermitten.frigga.runtime.type.FunctionType
import me.bristermitten.frigga.runtime.type.NothingType
import me.bristermitten.frigga.runtime.type.functionType
import me.bristermitten.frigga.transform.load
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.atn.PredictionMode
import org.antlr.v4.runtime.misc.ParseCancellationException
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class FriggaRuntime
{
    private val namespaces = mutableMapOf<String, FriggaContext>()
    private val globalContext = FriggaContext("")


    init
    {
        namespaces[""] = globalContext
        loadTypes()
        loadStdLib()
    }

    private fun loadStdLib()
    {
        val resource =
            javaClass.classLoader.getResource("std") ?: throw NoSuchElementException("Could not get std folder.")
        File(resource.toURI()).listFiles()?.forEach {
            val result = execute(it.readText(), it.name)
            result.exceptions.forEach { exception ->
                exception.printStackTrace()
            }
        }

        val ifProperty = getNamespace(STD_NAMESPACE).findProperty(IF_NAME)!!

        val ifSignature = Signature(
            emptyMap(),
            mapOf(
                "test" to BoolType,
                "run" to FunctionType(Signature(emptyMap(), emptyMap(), AnyType))
            ),
            AnyType
        )
        val ifFunction = Function(IF_NAME, ifSignature, listOf(
            singleCommand { stack, friggaContext ->
                val condition = friggaContext.findParameter("test")!!
                val run = friggaContext.findParameter("run")!!
                if (condition.value as Boolean)
                {
                    (run.value as Function).call(null, stack, friggaContext, emptyList())
                }
            }
        ))
        ifFunction.init(globalContext)
        ifProperty.value = Value(FunctionType(ifSignature), ifFunction)


        val loopProperty = namespaces[STD_NAMESPACE]!!.findProperty(LOOP_NAME)!!
        val loopSignature = Signature(
            emptyMap(),
            mapOf(
                "test" to functionType(returned = BoolType),
                "run" to functionType(returned = NothingType)
            ),
            AnyType
        )
        val loopFunction = Function(LOOP_NAME, loopSignature, listOf(
            singleCommand { stack, context ->
                val run = context.findParameter("run")!!

                val condition = context.findParameter("test")!!
                val conditionFunction = condition.value as Function
                fun eval(): Boolean
                {
                    conditionFunction.call(null, stack, context, emptyList())
                    return stack.pull().value as Boolean
                }
                while (eval())
                {
                    (run.value as Function).call(null, stack, context, emptyList())
                }
            }
        ))
        loopFunction.init(globalContext)
        loopProperty.value = Value(FunctionType(loopSignature), loopFunction)


    }

    @OptIn(ExperimentalTime::class)
    fun execute(friggaCode: String, fileName: String? = null): FullExecutionResult
    {
        val (lexer, lexTime) = measureTimedValue {
            FriggaLexer(CharStreams.fromString(friggaCode))
        }
        lexer.removeErrorListeners()
        lexer.addErrorListener(ThrowingErrorListener)

        val stream = CommonTokenStream(lexer)

        val (friggaFile, parseTime) = measureTimedValue {
            val parser = FriggaParser(stream)
            parser.removeErrorListeners()
            parser.addErrorListener(ThrowingErrorListener)
            parser.interpreter.predictionMode = PredictionMode.SLL
            try
            {
                parser.friggaFile()
            } catch (cancel: ParseCancellationException)
            {
                println("Error in $fileName")
                throw cancel
            } catch (ex: Exception)
            {
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

    fun reset()
    {
        namespaces.values
            .filter { it.name != STD_NAMESPACE }
            .forEach(FriggaContext::reset)
    }

    private fun getNamespace(namespace: String) = namespaces.getOrPut(namespace) { FriggaContext(namespace) }

    private fun process(file: FriggaFile): ExecutionResult
    {
        val exceptions = mutableListOf<Exception>()
        val namespace = file.namespace

        val context = if (namespace != null)
        {
            getNamespace(namespace.name)
        } else
        {
            this.globalContext
        }

        file.using.forEach {
            if (it is JVMNamespace)
            {
                context.defineType(getJVMType(it.jvmClass))
            } else
            {
                context.use(getNamespace(it.name))
            }
        }

        for (line in file.content)
        {
            try
            {
                line.command.eval(context.stack, context)
            } catch (e: Throwable)
            {
                exceptions += ExecutionException(e, line.position ?: Position.INVALID, line.text ?: "")
                break
            }
        }
        return ExecutionResult(
            context.stack.toList(),
            exceptions
        )
    }

}
