package me.bristermitten.frigga.ast.element

import me.bristermitten.frigga.ast.element.function.Function

sealed class Type(
    override val name: String,
    protected val typeFunctions: MutableMap<String, Function> = mutableMapOf()
) : Named {
    override fun toString() = name
    val functions: Map<String, Function> = typeFunctions

    open infix fun union(other: Type): Type = AnyType
}

object IntType : Type("Int") {
    init {
//        typeFunctions["add"] = Function()
    }
}

object DecType : Type("Dec")

object AnyType : Type("Any")

internal data class SimpleType(override val name: String) : Type(name)
internal data class FunctionType(override val name: String) : Type(name)

enum class TypeRelationship {
    Same,
    SuperType,
    SubType
}
