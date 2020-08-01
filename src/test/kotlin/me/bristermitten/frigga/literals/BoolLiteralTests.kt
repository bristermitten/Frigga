package me.bristermitten.frigga.literals

import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.runtime.data.boolValue
import me.bristermitten.frigga.runtime.data.charValue
import me.bristermitten.frigga.runtime.data.stringValue
import org.junit.jupiter.api.Test

class BoolLiteralTests : FriggaTest() {

    @Test
    fun `Assert Correct Functionality of Basic Bool Literal`() {
        val code = """
            bool = true
            bool
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe boolValue(true)
    }

    @Test
    fun `Assert Correct Functionality of Negated Bool Literal`() {
        val code = """
            bool = false
            !bool
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe boolValue(true)
    }
    @Test
    fun `Assert Correct Functionality of Repeated Negation`() {
        val code = """
            bool = false
            !!!bool
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe boolValue(true)
    }
}
