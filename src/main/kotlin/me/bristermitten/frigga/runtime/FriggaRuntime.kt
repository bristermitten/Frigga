package me.bristermitten.frigga.runtime

import FriggaLexer
import FriggaParser
import me.bristermitten.frigga.ast.transformer.FriggaFileTransformer
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

/**
 * @author AlexL
 */
class FriggaRuntime
{
	fun execute(source: String, sourceName: String? = null)
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

		val transformed = FriggaFileTransformer.transform(friggaFile)
		for (node in transformed.content)
		{
			try
			{
				node.element.execute()
			} catch (exception: Exception)
			{
				throw ExecutionException(node, exception)
			}
		}
	}
}
