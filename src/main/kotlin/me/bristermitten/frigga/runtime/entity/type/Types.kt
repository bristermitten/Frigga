package me.bristermitten.frigga.runtime.entity.type

object Types {
    private val types: Map<String, Type> = mapOf(
        AnyType.name to AnyType,
        NothingType.name to NothingType,
        NothingType.alias to NothingType,
        IntType.name to IntType,
        DoubleType.name to DoubleType,
        BoolType.name to BoolType,
        StringType.name to StringType
    )

    fun byName(name: String) = types[name] ?: throw IllegalArgumentException("No such type $name")
}
