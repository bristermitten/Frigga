package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.ast.Node

/**
 * @author AlexL
 */
class FriggaFile(
	val using: Set<String>, //TODO
	val content: List<Node>
)
{
}
