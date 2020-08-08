package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.ast.Node

/**
 * @author AlexL
 */
class ExecutionException(node: Node, parent: Exception) :
	Exception("Exception at ${node.position.line}:${node.position.col} (\"${node.text}\"", parent)
