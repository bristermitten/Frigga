package me.bristermitten.frigga.rewrite.identifier

object GetIdentifier : Identifier {
    override val name: String = "get"

    override fun matches(name: String): Boolean {
        return name == this.name
    }
}
