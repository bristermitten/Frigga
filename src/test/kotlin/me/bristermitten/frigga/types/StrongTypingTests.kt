package me.bristermitten.frigga.types

import io.kotest.matchers.collections.shouldNotBeEmpty
import me.bristermitten.frigga.FriggaTest
import org.junit.jupiter.api.Test

class StrongTypingTests : FriggaTest() {

    @Test
    fun `Assert that String cannot be substituted for Int`() {
        val code = """
            takeInt = (value::Int) -> _ {
                #do nothing
            }
            takeInt("Hello")
        """.trimIndent()

        val result = runtime.execute(code, "strong-typing")
        result.exceptions.shouldNotBeEmpty()
    }
}
