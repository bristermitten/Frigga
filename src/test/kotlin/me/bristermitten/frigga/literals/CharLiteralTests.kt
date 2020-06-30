package me.bristermitten.frigga.literals

import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.runtime.data.charValue
import me.bristermitten.frigga.runtime.data.stringValue
import org.junit.jupiter.api.Test

class CharLiteralTests : FriggaTest() {

    @Test
    fun `Assert Correct Functionality of Basic Char Literal`() {
        val code = """
            char = 'A'
            char
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe charValue('A')
    }

    @Test
    fun `Assert Correct Functionality of Char Literal Concatenation`() {
        val code = """
            someChar = 'A'
            someChar + 'B'
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe stringValue("AB")
    }
}
