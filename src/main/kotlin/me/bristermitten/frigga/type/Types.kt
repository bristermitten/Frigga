package me.bristermitten.frigga.type

object Types {

    private val types = mapOf(
        IntType.name to IntType,
        AnyType.name to AnyType,
        NothingType.name to NothingType
    )

    fun typeOf(name: String) = types[name] ?: throw IllegalArgumentException("No such type $name")
}
