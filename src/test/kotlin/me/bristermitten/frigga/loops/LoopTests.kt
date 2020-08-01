package me.bristermitten.frigga.loops

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.runtime.data.intValue
import org.junit.jupiter.api.Test

class LoopTests : FriggaTest()
{

    @Test
    fun `Test Basic Looping `()
    {
        val code = """
        use std

        mutable test = true
        loop(() -> test, {
            test = false
        })
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)
        result.exceptions.shouldBeEmpty()
        result.leftoverStack.shouldBeEmpty()
    }

    @Test
    fun `Test Repeated Looping`()
    {
        val code = """
        use std

        mutable count = 0
        mutable test = true
        loop(() -> test, {
            count = count + 1
            count
            if(count == 5, {
                test = false
            })
        })
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)
        result.exceptions.shouldBeEmpty()
        result.leftoverStack shouldBe listOf(5, 4, 3, 2, 1) //first in first out, so backwards
            .map(Int::toLong)
            .map(::intValue)
    }
}
