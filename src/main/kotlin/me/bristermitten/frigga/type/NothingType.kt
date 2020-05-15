package me.bristermitten.frigga.type

import me.bristermitten.frigga.Function

object NothingType : Type {
    override val name: String = "Nothing"
    override val isArray: Boolean  = false

    override fun extends(potentialParent: Type): Boolean = true
    override val functions: List<Function> = emptyList()
}
