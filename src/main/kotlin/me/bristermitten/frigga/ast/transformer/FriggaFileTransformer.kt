package me.bristermitten.frigga.ast.transformer

import FriggaParser
import me.bristermitten.frigga.runtime.FriggaFile

/**
 * @author AlexL
 */
object FriggaFileTransformer
{
	fun transform(node: FriggaParser.FriggaFileContext): FriggaFile
	{
		val header = node.header() //TODO
		val body = node.body()

		val lines = body.lines().line().map(Transformers::transform)

		return FriggaFile(
			emptySet(), //TODO
			lines
		)
	}
}
