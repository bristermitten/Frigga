package me.bristermitten.frigga.type

import me.bristermitten.frigga.Function

object IntType : Type {
    override val name = "Int"
    override val isArray = false

    override fun extends(potentialParent: Type): Boolean {
        return potentialParent is IntType
    }
    override val functions: List<Function> = emptyList()
}
