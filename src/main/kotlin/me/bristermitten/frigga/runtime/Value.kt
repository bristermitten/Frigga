package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.ast.element.Type

data class Value(val type: Type, val value: Any)
