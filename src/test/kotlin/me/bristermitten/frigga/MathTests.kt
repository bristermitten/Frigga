package me.bristermitten.frigga

import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.runtime.decValue
import me.bristermitten.frigga.runtime.intValue
import org.junit.jupiter.api.Test

class MathTests : FriggaTest() {

    @Test
    fun `Assert Simple Integer Addition Functioning Correctly`() {
        val code = """
            x = 3
            x + 1
        """.trimIndent()

        val result = runtime.execute(code, "math")
        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(4)
    }

    @Test
    fun `Assert More Complex Integer Addition Functioning Correctly`() {
        val code = """
            x = 3
            x + x + 5 + (x + 1)
        """.trimIndent()
        val result = runtime.execute(code, "math")

        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(15)
    }

    @Test
    fun `Assert Simple Integer Subtraction Functioning Correctly`() {
        val code = """
            x = 3
            x - 1
        """.trimIndent()
        val result = runtime.execute(code, "math")

        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(2)
    }


    @Test
    fun `Assert More Complex Integer Subtraction Functioning Correctly`() {
        val code = """
            x = 3
            x - x - 5 - (x - -1)
        """.trimIndent()
        val result = runtime.execute(code, "math")

        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(-7)
    }

    @Test
    fun `Assert Simple Decimal Addition Functioning Correctly`() {
        val code = """
            x = 3.0
            x + 1
        """.trimIndent()
        val result = runtime.execute(code, "math")
        handleExceptions(result)
        result.leftoverStack.first() shouldBe decValue(4.0)
    }
}
