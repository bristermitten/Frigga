package me.bristermitten.frigga.math

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.RANDOM_TEST_COUNT
import me.bristermitten.frigga.runtime.data.decValue
import me.bristermitten.frigga.runtime.data.intValue
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

class AdditionTests : FriggaTest()
{

    @Test
    fun `Assert Simple Integer Addition Functioning Correctly`()
    {
        val code = """
            x = 3
            x + 1
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(3 + 1)
    }

    @Test
    fun `Assert More Complex Integer Addition Functioning Correctly`()
    {
        val x = 3
        val code = """
            x = $x
            (x + x) + 5 + x
        """.trimIndent()
        val result = runtime.execute(code)

        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue((x + x + 5 + x).toLong())
    }


    @Test
    fun `Assert Simple Decimal Addition Functioning Correctly`()
    {
        val code = """
            x = 3.0
            x + 1
        """.trimIndent()
        val result = runtime.execute(code)
        result.exceptions.shouldBeEmpty()

        result.leftoverStack.first() shouldBe decValue(4.0)
    }

    @Test
    fun `Assert Simple Decimal Reassignment Functioning Correctly`()
    {
        val code = """
            mutable x = 3.0
            x = 4
            x
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)
        result.leftoverStack.first() shouldBe decValue(4.0)
    }

    @RepeatedTest(RANDOM_TEST_COUNT)
    fun `Assert Random Decimal Addition Functioning Correctly`()
    {
        val start = Math.random()
        val param1 = Math.random()
        val param2 = Math.random()

        val code = """
            use std
            x = $start
            x + $param1 + $param2
            
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)
        result.leftoverStack.first() shouldBe decValue(start + param1 + param2)
    }
}
