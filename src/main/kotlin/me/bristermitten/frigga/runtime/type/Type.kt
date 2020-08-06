package me.bristermitten.frigga.runtime.type

/**
 * @author AlexL
 */
open class Type(
	val name: String,
	val parent: Type? = AnyType
)
