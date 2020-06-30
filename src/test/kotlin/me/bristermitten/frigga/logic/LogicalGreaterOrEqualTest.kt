package me.bristermitten.frigga.logic

import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.RANDOM_TEST_COUNT
import me.bristermitten.frigga.runtime.data.boolValue
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class LogicalGreaterOrEqualTest : FriggaTest() {

    @Test
    fun `Assert Correct Functionality of Greater Or Equal Operator with Ints`() {
        val code = """
            4 >= 3
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe boolValue(true)
    }

    @Test
    fun `Assert Correct Functionality of Greater Or Equal Operator with Ints 2`() {
        val code = """
            4 >= 4
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe boolValue(true)
    }

    @Test
    fun `Assert Correct Functionality of Greater Or Equal Operator with Int and Dec`() {
        val code = """
            4 >= 3.5
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe boolValue(true)
    }

    @Test
    fun `Assert Correct Functionality of Greater Or Equal Operator with Int and Dec 2 `() {
        val code = """
            9.0 >= 9
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe boolValue(true)
    }

    @RepeatedTest(RANDOM_TEST_COUNT)
    fun `Assert Correct Functionality of Greater Or Equal Operator with Random Decs`() {
        val a = Math.random()
        val b = Math.random()
        val code = """
            $a >= $b
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe boolValue(a >= b)
    }
}
