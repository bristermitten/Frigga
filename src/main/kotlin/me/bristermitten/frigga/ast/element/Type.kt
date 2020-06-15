package me.bristermitten.frigga.ast.element

internal sealed class Type(
    override val name: String
) : Named

internal data class SimpleType(override val name: String) : Type(name)
internal data class FunctionType(override val name: String) : Type(name)

enum class TypeRelationship {
    Same,
    SuperType,
    SubType
}
