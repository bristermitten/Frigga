package me.bristermitten.frigga.rewrite.identifier

interface Identifier {
    val name: String
    fun matches(name: String): Boolean
}
