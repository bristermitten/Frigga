package me.bristermitten.frigga.ast

/**
 * @author AlexL
 */
data class Node(
	val text: String,
	val position: Position,
	val element: Element<*>
)
