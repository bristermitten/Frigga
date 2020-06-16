package me.bristermitten.frigga.ast.element

import me.bristermitten.frigga.ast.element.function.Function

sealed class Type(
    override val name: String,
    protected val typeFunctions: MutableMap<String, Function> = mutableMapOf()
) : Named {

//    override fun toString() = name


    val functions: Map<String, Function> = typeFunctions

    open infix fun union(other: Type): Type = AnyType
}

object IntType : Type("Int") {

    override fun union(other: Type): Type {
        if (other is DecType) {
            return DecType
        }
        if (other is IntType) {
            return IntType
        }
        return super.union(other)
    }
}

object DecType : Type("Dec") {
    override fun union(other: Type): Type {
        if (other is IntType || other is DecType) {
            return DecType
        }
        return super.union(other)
    }
}

object AnyType : Type("Any")

internal data class SimpleType(override val name: String) : Type(name)
internal data class FunctionType(override val name: String) : Type(name)

enum class TypeRelationship {
    Same,
    SuperType,
    SubType
}
