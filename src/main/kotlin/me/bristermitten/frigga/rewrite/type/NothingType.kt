package me.bristermitten.frigga.rewrite.type

import me.bristermitten.frigga.rewrite.function.Function
import me.bristermitten.frigga.rewrite.identifier.Identifier
import me.bristermitten.frigga.rewrite.type.Type

object NothingType : Type {
    override val name: String = "Nothing"
    override val members: Map<Identifier, Function> = emptyMap()
}
