package me.bristermitten.frigga.runtime.type

import me.bristermitten.frigga.runtime.FriggaContext

data class TupleType(
    val types: Map<String, Type>
) : Type(types.values.joinToString(prefix = "(", postfix = ")")) {
    override fun reestablish(context: FriggaContext): Type {
        return TupleType(
            types.mapValues { it.value.reestablish(context) }
        )
    }
}
