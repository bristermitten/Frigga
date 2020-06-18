package me.bristermitten.frigga.math

import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.runtime.decValue
import me.bristermitten.frigga.runtime.intValue
import org.junit.jupiter.api.Test

class MultiplicationTests : FriggaTest() {

    @Test
    fun `Assert Simple Integer Multiplication Functioning Correctly`() {
        val code = """
            x = 3
            x * 2
        """.trimIndent()
        val result = runtime.execute(code, "math")

        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(3 * 2 )
    }

    @Test
    fun `Assert More Complex Integer Multiplication Functioning Correctly`() {
        val code = """
            x = 3
            x * x * 5 * x * -1
        """.trimIndent()
        val result = runtime.execute(code, "math")

        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(3 * 3 * (5 * 3 * -1))
    }

}
