package me.bristermitten.frigga.runtime.entity.type

import me.bristermitten.frigga.runtime.entity.Property

abstract class Type {
    abstract val name: String
    open val properties: Map<String, Property> = emptyMap()

    open fun matches(other: Type) = other == this

    /**
     * Provide a common super type that both the called type and [other] are subtypes of.
     * It is assumed that [other] will always `!=` this type.
     */
    open fun commonSuperType(other: Type): Type = AnyType
}

open class BasicType(override val name: String) : Type() {
    override fun toString() = name
}

object AnyType : BasicType("Any") {
    override fun matches(other: Type): Boolean = true
}

object NothingType : BasicType("Nothing") {
    const val alias: String = "_"
    override fun matches(other: Type): Boolean = true
}
