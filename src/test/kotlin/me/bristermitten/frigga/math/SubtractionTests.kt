package me.bristermitten.frigga.math

import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.RANDOM_TEST_COUNT
import me.bristermitten.frigga.runtime.data.decValue
import me.bristermitten.frigga.runtime.data.intValue
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class SubtractionTests : FriggaTest() {
    @Test
    fun `Assert Simple Integer Subtraction Functioning Correctly`() {
        val code = """
            x = 3
            x - 1
        """.trimIndent()
        val result = runtime.execute(code)

        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(2)
    }

    @Test
    fun `Assert More Complex Integer Subtraction Functioning Correctly`() {
        val code = """
            x = 3
            x - x - 5 - (x - -1)
        """.trimIndent()
        val result = runtime.execute(code)

        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(3 - 3 - 5 - (3 - -1))
    }

    @Test
    fun `Assert Simple Decimal Subtraction Functioning Correctly`() {
        val code = """
            x = 3.0
            x - 1.5
        """.trimIndent()
        val result = runtime.execute(code)

        handleExceptions(result)
        result.leftoverStack.first() shouldBe decValue(1.5)
    }

    @Test
    fun `Assert More Complex Decimal Subtraction Functioning Correctly`() {
        val code = """
            x = 15.9
            x - x - 5.2 - (x - -1.3)
        """.trimIndent()
        val result = runtime.execute(code)

        handleExceptions(result)
        result.leftoverStack.first() shouldBe decValue(15.9 - 15.9 - 5.2 - (15.9 - -1.3))
    }


    @RepeatedTest(RANDOM_TEST_COUNT)
    fun `Assert Random Decimal Subtraction Functioning Correctly`() {
        val start = Math.random()
        val param1 = Math.random()
        val param2 = Math.random()

        val code = """
            x = $start
            x - $param1 - $param2
        """.trimIndent()
        val result = runtime.execute(code)

        handleExceptions(result)
        result.leftoverStack.first() shouldBe decValue(start - param1 - param2)
    }
}
