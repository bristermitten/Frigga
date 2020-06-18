package me.bristermitten.frigga

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import me.bristermitten.frigga.runtime.intValue
import org.junit.jupiter.api.Test

class LambdaTests : FriggaTest() {
    @Test
    fun `Test correct handling of a Simple Lambda that yields a value`() {
        val code = """
            getValue = {
                yield(3)
            }
            getValue()
        """.trimIndent()

        val result = runtime.execute(code, "function")
        handleExceptions(result)
        result.leftoverStack shouldContain intValue(3)
    }
    @Test
    fun `Test correct handling of an expression lambda that yields a value`() {
        val code = """
            getValue = () -> 100
            x = getValue()
            x
        """.trimIndent()

        val result = runtime.execute(code, "function")
        handleExceptions(result)
        result.leftoverStack shouldContain intValue(100)
        result.leftoverStack shouldNotContain intValue(99)
    }
}
