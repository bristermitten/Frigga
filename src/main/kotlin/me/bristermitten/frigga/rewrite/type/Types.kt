package me.bristermitten.frigga.rewrite.type

object Types {

    private val types = mapOf(
        IntType.name to IntType,
        AnyType.name to AnyType,
        NothingType.name to NothingType
    )

    private val jvmTypes = mutableMapOf<String, JVMType>()

    private fun jvmTypeOf(name: String) = jvmTypes[name] ?: JVMType(Class.forName(name)).also {
        jvmTypes[name] = it
    }
    fun typeOf(name: String) = types[name] ?: jvmTypeOf(name)
}
