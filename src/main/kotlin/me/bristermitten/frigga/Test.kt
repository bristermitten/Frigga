package me.bristermitten.frigga

import FriggaLexer
import FriggaParser
import me.bristermitten.frigga.context.FriggaContext
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import kotlin.system.measureTimeMillis


object Test {

    @JvmStatic
    fun main(args: Array<String>) {
        val code = ClassLoader.getSystemClassLoader().getResourceAsStream("samples/simple.fg")
        val friggaLexer = FriggaLexer(CharStreams.fromStream(code))
        val parser = FriggaParser(CommonTokenStream(friggaLexer))
        val parseTree = parser.friggaFile()
        val ast = parseTree.toAST(true)

        val time = measureTimeMillis {
            val context = FriggaContext(emptyMap(), emptyMap())
//            context.function("print", Function(
//                listOf(
//
//                )
//            ))
            ast.statements.forEach {
                it.execute(context)
            }
        }
        println("$time ms")
    }
}
