package me.bristermitten.frigga.type

import me.bristermitten.frigga.Function

interface Type {
    val name: String
    val isArray: Boolean

    fun extends(potentialParent: Type): Boolean
    val functions: List<Function>
}
