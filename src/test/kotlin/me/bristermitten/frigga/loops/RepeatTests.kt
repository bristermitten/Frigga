package me.bristermitten.frigga.loops

import StringType
import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.runtime.data.Value
import org.junit.jupiter.api.Test

/**
 * @author AlexL
 */
class RepeatTests : FriggaTest()
{

    @Test
    fun `Assert Repeat Function working correctly`()
    {
        val code = """
        use std

        repeat(10, {
            "Hello"
        })
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.size shouldBe 10
        result.leftoverStack shouldBe Array(10) { "Hello" }.map { Value(StringType, it) }
    }
}
