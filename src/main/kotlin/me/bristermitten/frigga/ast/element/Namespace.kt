package me.bristermitten.frigga.ast.element

data class Namespace(override val name: String) : Named

val NAMESPACE_FORMAT = "((JVM:)?[a-zA-Z0-9.]+)|([a-zA-Z0-9/]+)".toRegex()
