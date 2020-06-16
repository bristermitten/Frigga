package me.bristermitten.frigga.ast.element

import me.bristermitten.frigga.ast.element.function.Signature
import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.function.FunctionValue

sealed class Type(
    override val name: String,
    protected val typeFunctions: MutableMap<String, FunctionValue> = mutableMapOf()
) : Named {

    init {
        _types[name] = this
    }

    val functions: Map<String, FunctionValue> = typeFunctions

    open infix fun union(other: Type): Type = AnyType
}

object IntType : Type("Int") {
    override fun union(other: Type): Type {
        return when (other) {
            is DecType -> DecType
            is IntType -> IntType
            else -> super.union(other)
        }
    }
}

object DecType : Type("Dec") {
    override fun union(other: Type): Type {
        return when (other) {
            is IntType, is DecType -> DecType
            else -> super.union(other)
        }
    }
}
object StringType : Type("String")

object AnyType : Type("Any")
object NothingType : Type("Nothing")

internal data class SimpleType(override val name: String) : Type(name)
internal data class FunctionType(val signature: Signature) : Type(signature.toString())

data class JVMType(val jvmClass: Class<*>) : Type(jvmClass.simpleName) {
    init {
        jvmClass.methods.forEach {
            typeFunctions[it.name] = FunctionValue(
                Signature(
                    emptyMap(),
                    it.parameters.map { param ->
                        param.name to (types[name] ?: JVMType(param.type))
                    }.toMap(),
                    (types[it.returnType.simpleName] ?: JVMType(it.returnType))
                ),
                listOf(object : Command() {
                    override fun eval(stack: Stack, context: FriggaContext) {
                        val upon = context.findProperty("__upon")!!.value
                        it.invoke(upon.value, *it.parameters.map {
                            val findProperty = context.findProperty(it.name)
                            findProperty?.value
                        }.toTypedArray())
                    }
                })
            )
        }
    }
}

object CallerType : Type("__Caller") {
    init {
        typeFunctions["break"] = FunctionValue(
            Signature(emptyMap(), emptyMap(), NothingType),
            listOf(
                object : Command() {
                    override fun eval(stack: Stack, context: FriggaContext) {
                        throw BreakException
                    }
                }
            )
        )
    }
}

object BreakException : Exception()

object OutputType : Type("Output") {
    init {
        typeFunctions["println"] = FunctionValue(
            Signature(emptyMap(), mapOf("value" to AnyType), NothingType),
            listOf(
                object : Command() {
                    override fun eval(stack: Stack, context: FriggaContext) {
                        val toPrint = context.findProperty("value")
                        require(toPrint != null) {
                            "Nothing provided for \"value\""
                        }
                        println(toPrint.value.value)
                    }
                }
            )
        )
    }
}

private val _types = mutableMapOf<String, Type>()
val types: Map<String, Type> = _types

enum class TypeRelationship {
    Same,
    SuperType,
    SubType
}
