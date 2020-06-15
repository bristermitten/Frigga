package me.bristermitten.frigga

import FriggaLexer
import FriggaParser
import me.bristermitten.frigga.ast.toAST
import me.bristermitten.frigga.scope.FriggaRuntime
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.atn.PredictionMode
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.system.measureTimeMillis


class LexTest {

    @ExperimentalStdlibApi
    @Test
    fun test() {
        val samples = File("examples/")
//        val files = samples.walkTopDown().filter {
//            it.isFile
//        }
        val files = listOf(samples.resolve("simple.fg"))
        files.forEach { file ->
            val friggaLexer = FriggaLexer(CharStreams.fromStream(file.inputStream()))

            val stream = CommonTokenStream(friggaLexer)
            val parseTime = measureTimeMillis {

                val parser = FriggaParser(stream)
                parser.interpreter.predictionMode = PredictionMode.SLL
                var friggaFile: FriggaParser.FriggaFileContext
                try {
                    friggaFile = parser.friggaFile() // STAGE 1
                } catch (ex: Exception) {
                    stream.seek(0) // rewind input stream
                    parser.reset()
                    parser.interpreter.predictionMode = PredictionMode.LL
                    friggaFile = parser.friggaFile() // STAGE 2
                }

                val ast = friggaFile.toAST(file.name)
                val runtime = FriggaRuntime()
                runtime.process(ast)
            }
            println("Parsed $file (${file.length()} bytes) in $parseTime ms")
        }

    }
}
