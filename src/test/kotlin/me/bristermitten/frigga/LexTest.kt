package me.bristermitten.frigga

import FriggaLexer
import FriggaParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.jupiter.api.Test
import java.io.File

class LexTest {

    @Test
    fun test() {
//        javaClass.getResourceAsStream("samples")
        val samples = File("examples/")
        samples.listFiles()?.forEach { file ->
            println(file)
            val friggaLexer = FriggaLexer(CharStreams.fromStream(file.inputStream()))

            println(
                FriggaParser(CommonTokenStream(friggaLexer)).friggaFile()
            )
        }

    }
}
