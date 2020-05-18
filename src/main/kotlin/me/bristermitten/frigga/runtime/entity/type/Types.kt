package me.bristermitten.frigga.runtime.entity.type

object Types {
    private val types: Map<String, Type> = mapOf(
        AnyType.name to AnyType,
        NothingType.name to NothingType,
        IntType.name to IntType,
        DoubleType.name to DoubleType,
        BoolType.name to BoolType
    )

    fun byName(name: String) = types[name] ?: throw IllegalArgumentException("No such type $name")
}
