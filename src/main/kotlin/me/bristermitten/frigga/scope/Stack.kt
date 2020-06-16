package me.bristermitten.frigga.scope

import java.util.*

class Stack(private val stack: Deque<Any> = ArrayDeque()) {

    fun peek(): Any? = stack.peek()
    fun pull(): Any = stack.poll()
    fun push(data: Any) = stack.push(data)

    override fun toString(): String {
        return stack.toString()
    }

    fun toList() = stack.toList()
    fun clear() = stack.clear()
}
