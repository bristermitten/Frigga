package me.bristermitten.frigga.ast.element

sealed class Namespace(override val name: String) : Named
class SimpleNamespace(name: String) : Namespace(name)
data class JVMNamespace(val jvmClass: Class<*>) : Namespace(jvmClass.name)

val JVM_NAMESPACE_FORMAT = "JVM:[a-zA-Z0-9.]+".toRegex()
val NAMESPACE_FORMAT = "[a-zA-Z0-9/]+".toRegex()
