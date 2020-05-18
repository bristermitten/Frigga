package me.bristermitten.frigga

import FriggaLexer
import FriggaParser
import me.bristermitten.frigga.loader.toAST
import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.entity.type.registerBuiltInTypes
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@ExperimentalTime
fun main() {
    val code = ClassLoader.getSystemClassLoader().getResourceAsStream("samples/simple.fg")
    val charStream = CharStreams.fromStream(code)

    val (friggaLexer, lexTime) = measureTimedValue {
        FriggaLexer(charStream)
    }
    println("Lexing took ${lexTime.inMilliseconds}ms")

    val (file, parseTime) = measureTimedValue {
        val parser = FriggaParser(CommonTokenStream(friggaLexer))
        val parseTree = parser.friggaFile()
        parseTree.toAST()
    }
    println("Parsed AST in ${parseTime.inMilliseconds}ms")


    val execTime = measureTimeMillis {
        val context = FriggaContext()
        registerBuiltInTypes(context)
        file.contents.forEach {
            it.initialize(context)
            it.eval(context.stack, context)
        }
    }

    println("Executed program in $execTime ms")
}
