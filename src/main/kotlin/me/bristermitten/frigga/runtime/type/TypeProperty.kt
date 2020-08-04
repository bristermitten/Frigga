package me.bristermitten.frigga.runtime.type

import me.bristermitten.frigga.runtime.data.Property

data class TypeProperty(
	val type: () -> Type,
	val property: Property
)
