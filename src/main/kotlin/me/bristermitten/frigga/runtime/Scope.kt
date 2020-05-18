package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.runtime.entity.Property

data class Scope(
    val name: String,
    val paramValues: MutableMap<String, Any> = mutableMapOf()
) {
    val properties: MutableMap<String, Property> = mutableMapOf()
}
