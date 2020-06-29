package me.bristermitten.frigga.runtime.data

sealed class Namespace(val name: String)
class SimpleNamespace(name: String) : Namespace(name)
data class JVMNamespace(val jvmClass: Class<*>) : Namespace(jvmClass.name)

val JVM_NAMESPACE_FORMAT = "JVM:[a-zA-Z0-9.]+".toRegex()
val NAMESPACE_FORMAT = "[a-zA-Z0-9/]+".toRegex()
