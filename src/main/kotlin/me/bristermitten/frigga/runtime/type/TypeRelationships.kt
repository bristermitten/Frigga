package me.bristermitten.frigga.runtime.type

enum class TypeRelationship {
    Same,
    Supertype,
    Subtype,

    /**
     * The 2 types share a parent.
     */
    Sibling,

    /**
     * The 2 types have no link (except AnyType)
     */
    NoRelationship
}

fun Type.distanceTo(other: Type): Int {
    return when {
        this === other -> 0
        this.isSubtypeOf(other) || other.isSubtypeOf(this) -> 1
        else -> Int.MAX_VALUE
    }
}

infix fun Type.relationshipTo(other: Type): TypeRelationship {
    if (this === other) return TypeRelationship.Same
    if (this.parent === other.parent) return TypeRelationship.Sibling
    val isSubtype = isSubtypeOf(other)
    if (isSubtype) {
        return TypeRelationship.Subtype
    }
    val isSupertype = other.isSubtypeOf(this)
    if (isSupertype) {
        return TypeRelationship.Supertype
    }
    return TypeRelationship.NoRelationship
}




