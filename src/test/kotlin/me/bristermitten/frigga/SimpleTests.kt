package me.bristermitten.frigga

import me.bristermitten.frigga.runtime.FriggaRuntime
import org.junit.jupiter.api.Test

class SimpleTest {

    @Test
    fun `Test`() {
        val code = """
            x = 3.0
            someFun = () -> _ { println(x + 1) }
            someFun()
        """.trimIndent()

        val result = FriggaRuntime().execute(code, "tests")
        for (exception in result.exceptions) {
            throw exception
        }
        result.leftoverStack.forEach {
            println(it)
        }

    }
}
