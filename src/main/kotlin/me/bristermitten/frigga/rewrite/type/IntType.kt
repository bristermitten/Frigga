package me.bristermitten.frigga.rewrite.type

import me.bristermitten.frigga.rewrite.function.Function
import me.bristermitten.frigga.rewrite.identifier.Identifier
import me.bristermitten.frigga.rewrite.type.Type

object IntType : Type {
    override val name = "Int"
    override val members: Map<Identifier, Function> = emptyMap()
}
