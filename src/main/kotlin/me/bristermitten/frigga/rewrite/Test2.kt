package me.bristermitten.frigga.rewrite

import me.bristermitten.frigga.Position
import me.bristermitten.frigga.context.FriggaContext
import me.bristermitten.frigga.context.GlobalContext
import me.bristermitten.frigga.rewrite.function.FunctionSignature
import me.bristermitten.frigga.rewrite.type.JVMType
import me.bristermitten.frigga.rewrite.type.Type

fun main() {
    val printStream = JVMType(System.out::class.java)
    printStream.members.forEach { (k, v) ->
        println("$k $v")
    }
    GlobalContext.registerVar("stdout", object : Variable {
        override val type: Type = printStream
        override val name: String = "stdout"
        override val signature: FunctionSignature = FunctionSignature(emptyList(), printStream)
        override val modifiers: Set<Modifier> = emptySet()

        override fun evaluate(context: FriggaContext): Any {
            return this
        }

        override val position: Position? = null
    })
    println(GlobalContext.resolveVariable("stdout"))
}
