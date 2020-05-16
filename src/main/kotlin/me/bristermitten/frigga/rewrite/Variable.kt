package me.bristermitten.frigga.rewrite

import me.bristermitten.frigga.rewrite.function.Function
import me.bristermitten.frigga.rewrite.type.Type

/**
 * Variable has 2 functions:
 * get()
 * and optional set(value)
 *
 */
interface Variable : Function {
    val type: Type
}
