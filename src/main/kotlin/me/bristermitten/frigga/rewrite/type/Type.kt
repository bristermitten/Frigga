package me.bristermitten.frigga.rewrite.type

import me.bristermitten.frigga.rewrite.function.Function
import me.bristermitten.frigga.rewrite.identifier.Identifier

/**
 * Represents a Type of a Variable, Parameter, or Return Value
 */
interface Type {
    val name: String
    val members: Map<Identifier, Function>
}
