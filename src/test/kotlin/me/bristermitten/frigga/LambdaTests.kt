package me.bristermitten.frigga

import io.kotest.matchers.collections.shouldContain
import me.bristermitten.frigga.runtime.data.intValue
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

        val result = runtime.execute(code)
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

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack shouldContain intValue(100)
    }
}
