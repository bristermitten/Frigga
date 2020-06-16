package me.bristermitten.frigga

import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.runtime.intValue
import org.junit.jupiter.api.Test

class MathTests : FriggaTest() {

    @Test
    fun `Assert Simple Addition Functioning Correctly`() {
        val code = """
            x = 3
            x + 1
        """.trimIndent()
        val result = runtime.execute(code, "math")

        result.leftoverStack.first() shouldBe intValue(4)
    }

    @Test
    fun `Assert More Complex Addition Functioning Correctly`() {
        val code = """
            x = 3
            x + x + 5 + (x + 1)
        """.trimIndent()
        val result = runtime.execute(code, "math")

        result.leftoverStack.first() shouldBe intValue(15)
    }

    @Test
    fun `Assert Simple Subtraction Functioning Correctly`() {
        val code = """
            x = 3
            x - 1
        """.trimIndent()
        val result = runtime.execute(code, "math")

        result.leftoverStack.first() shouldBe intValue(2)
    }


    @Test
    fun `Assert More Complex Subtraction Functioning Correctly`() {
        val code = """
            x = 3
            x - x - 5 - (x - -1)
        """.trimIndent()
        val result = runtime.execute(code, "math")

        result.leftoverStack.first() shouldBe intValue(-7)
    }
}
