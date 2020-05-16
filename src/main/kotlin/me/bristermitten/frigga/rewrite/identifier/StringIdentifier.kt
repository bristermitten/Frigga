package me.bristermitten.frigga.rewrite.identifier

data class StringIdentifier(override val name: String) : Identifier {
    override fun matches(name: String): Boolean {
        return name == this.name
    }
}
