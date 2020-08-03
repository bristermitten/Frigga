package me.bristermitten.frigga.runtime.type

import me.bristermitten.frigga.runtime.FriggaContext

sealed class TupleType(
    open val types: Map<String, Type>
) : Type(types.values.joinToString(prefix = "(", postfix = ")"))

data class IndexedTupleType(val typeList: List<Type>) :
    TupleType(
        typeList.mapIndexed { index, type -> index.toString() to type }.toMap()
    )
{
    override fun reestablish(context: FriggaContext): Type
    {
        return IndexedTupleType(typeList.map {
            it.reestablish(context)
        })
    }
}

data class NamedTupleType(override val types: Map<String, Type>) : TupleType(types)
{
    override fun reestablish(context: FriggaContext): Type
    {
        return NamedTupleType(types.mapValues {
            it.value.reestablish(context)
        })
    }
}
