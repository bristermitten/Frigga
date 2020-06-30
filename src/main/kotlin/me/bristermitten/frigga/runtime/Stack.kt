package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.runtime.data.Value
import java.util.*

class Stack(private val stack: Deque<Value> = ArrayDeque()) {

    fun peek(): Value? = stack.peek()
    fun pull(): Value = stack.poll()
    fun push(data: Value) {
        stack.push(data)
    }

    override fun toString(): String {
        return stack.toString()
    }

    @JvmSynthetic //Workaround for JVMType
    fun toList() = stack.toList()
    fun clear() = stack.clear()
}
