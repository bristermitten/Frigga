package me.bristermitten.frigga.math

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.RANDOM_TEST_COUNT
import me.bristermitten.frigga.runtime.decValue
import me.bristermitten.frigga.runtime.intValue
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import kotlin.math.pow
import kotlin.math.roundToLong

class ExponentTests : FriggaTest() {

    @Test
    fun `Assert Simple Integer Exponentiation Functioning Correctly`() {
        val code = """
            x = 3
            x ^ 2
        """.trimIndent()

        val result = runtime.execute(code, "math")
        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(3.0.pow(2.0).roundToLong())
    }

    @Test
    fun `Assert More Complex Integer Exponentiation Functioning Correctly`() {
        val code = """
            x = 3
            x ^ x ^ 5 ^ (x ^ 1)
        """.trimIndent()
        val result = runtime.execute(code, "math")

        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(3.0.pow(3.0).pow(5.0).pow(4.0).roundToLong())
    }


    @Test
    fun `Assert Simple Decimal Exponentiation Functioning Correctly`() {
        val code = """
            x = 3.0
            x ^ 3.2
        """.trimIndent()
        val result = runtime.execute(code, "math")
        result.exceptions.shouldBeEmpty()
        result.leftoverStack.first() shouldBe decValue(3.0.pow(3.2))
    }

    @RepeatedTest(RANDOM_TEST_COUNT)
    fun `Assert Random Decimal Exponentiation Functioning Correctly`() {
        val start = Math.random()
        val param1 = Math.random()
        val param2 = Math.random()

        val code = """
            x = $start
            x ^ $param1 ^ $param2
        """.trimIndent()
        val result = runtime.execute(code, "math")

        handleExceptions(result)
        result.leftoverStack.first() shouldBe decValue(start.pow(param1).pow(param2))
    }
}
