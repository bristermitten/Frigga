package me.bristermitten.frigga.runtime.type

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.data.function.Signature

data class FunctionType(
    val signature: Signature
) : Type(
    signature.params.values.joinToString(
        prefix = "(",
        postfix = ")"
    ) { it.toString() } + " -> " + signature.returned.name
) {
    override fun accepts(other: Type): Boolean {
        if (other is FunctionType) {
            return signature.matches(other.signature)
        }

        return super.accepts(other)
    }

    override fun toString() = name

    override fun reestablish(context: FriggaContext): Type {
        return FunctionType(
            Signature(
                signature.typeSignature.mapValues { it.value.reestablish(context) },
                signature.params.mapValues { it.value.reestablish(context) },
                signature.returned.reestablish(context)
            )
        )
    }
}

fun functionType(
    type: Map<String, Type> = emptyMap(),
    params: Map<String, Type> = emptyMap(),
    returned: Type = NothingType
): FunctionType {
    return FunctionType(
        Signature(
            type, params, returned
        )
    )
}
