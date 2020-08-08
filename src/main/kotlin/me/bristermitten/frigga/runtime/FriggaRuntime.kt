package me.bristermitten.frigga.runtime

import FriggaLexer
import FriggaParser
import me.bristermitten.frigga.ast.transformer.FriggaFileTransformer
import me.bristermitten.frigga.runtime.context.Context
import me.bristermitten.frigga.runtime.value.Value
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

/**
 * @author AlexL
 */
class FriggaRuntime
{
	fun execute(source: String, sourceName: String? = null): ExecutionResult
	{
		val friggaFile = loadAST(source, sourceName)
		val context = Context()
		val leftoverStack = mutableListOf<Value>()

		for (node in friggaFile.content)
		{
			try
			{
				val element = node.element
				val value = element.execute(context)
				if (value is Value)
				{
					leftoverStack += value
				}
			} catch (exception: Exception)
			{
				throw ExecutionException(node, exception)
			}
		}

		return ExecutionResult(leftoverStack)
	}

	private fun loadAST(source: String, sourceName: String?): FriggaFile
	{
		val stream = if (sourceName != null)
		{
			CharStreams.fromString(source, sourceName)
		} else
		{
			CharStreams.fromString(source)
		}

		val lexer = FriggaLexer(stream)
		val tokens = CommonTokenStream(lexer)

		val parser = FriggaParser(tokens)

		val friggaFile = parser.friggaFile()
		return FriggaFileTransformer.transform(friggaFile)
	}
}
