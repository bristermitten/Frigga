package me.bristermitten.frigga.math

import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.RANDOM_TEST_COUNT
import me.bristermitten.frigga.runtime.decValue
import me.bristermitten.frigga.runtime.intValue
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class DivisionTests : FriggaTest() {

    @Test
    fun `Assert Simple Integer Division Functioning Correctly`() {
        val code = """
            x = 4
            x / 2
        """.trimIndent()
        val result = runtime.execute(code, "math")

        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(4 / 2)
    }

    @Test
    fun `Assert More Complex Integer Division Functioning Correctly`() {
        val code = """
            x = 4000
            x / 400 / 5 / 2 
        """.trimIndent()
        val result = runtime.execute(code, "math")

        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(1)
    }

    @Test
    fun `Assert Simple Decimal Division Functioning Correctly`() {
        val code = """
            x = 3.5
            x / 2.9
        """.trimIndent()
        val result = runtime.execute(code, "math")

        handleExceptions(result)
        result.leftoverStack.first() shouldBe decValue(3.5 / 2.9)
    }

    @Test
    fun `Assert More Complex Decimal Division Functioning Correctly`() {
        val code = """
            x = 36841.871
            x / 5.1486 / -1.41875
        """.trimIndent()
        val result = runtime.execute(code, "math")

        handleExceptions(result)
        result.leftoverStack.first() shouldBe decValue(36841.871 / 5.1486 / -1.41875)
    }

    @RepeatedTest(RANDOM_TEST_COUNT)
    fun `Assert Random Decimal Division Functioning Correctly`() {
        val start = Math.random()
        val param1 = Math.random()
        val param2 = Math.random()

        val code = """
            x = $start
            x / $param1 / $param2
        """.trimIndent()
        val result = runtime.execute(code, "math")

        handleExceptions(result)
        result.leftoverStack.first() shouldBe decValue(start / param1 / param2)
    }

}
