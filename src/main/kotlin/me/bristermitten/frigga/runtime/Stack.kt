package me.bristermitten.frigga.runtime

import java.util.*

class Stack {
    private val stack = ArrayDeque<Any>()

    fun peek(): Any? = stack.peek()

    fun pull(): Any = stack.poll()

    fun push(value: Any) = stack.push(value)

    override fun toString() = stack.toString()

}
