package me.bristermitten.frigga.ast.element

sealed class Type(
    override val name: String
) : Named {
    override fun toString() = name
}

object IntType : Type("Int")
object DecType : Type("Dec")

internal data class SimpleType(override val name: String) : Type(name)
internal data class FunctionType(override val name: String) : Type(name)

enum class TypeRelationship {
    Same,
    SuperType,
    SubType
}
