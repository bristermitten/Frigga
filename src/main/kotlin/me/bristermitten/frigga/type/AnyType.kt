package me.bristermitten.frigga.type

import me.bristermitten.frigga.Function

object AnyType : Type {
    override val name: String = "Any"
    override val isArray: Boolean = false
    override fun extends(potentialParent: Type) = true
    override val functions: List<Function> = emptyList()
}
