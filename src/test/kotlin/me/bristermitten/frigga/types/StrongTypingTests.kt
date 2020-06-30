package me.bristermitten.frigga.types

import io.kotest.matchers.collections.shouldBeEmpty
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
    @Test
    fun `Assert that Dec cannot be substituted for Int`() {
        val code = """
            takeInt = (value::Int) -> _ {
                #do nothing
            }
            takeInt(3.0)
        """.trimIndent()

        val result = runtime.execute(code, "strong-typing")
        result.exceptions.shouldNotBeEmpty()
    }
    @Test
    fun `Assert that Int can be substituted for Dec`() {
        val code = """
            takeInt = (value::Dec) -> _ {
                #do nothing
            }
            takeInt(3)
        """.trimIndent()

        val result = runtime.execute(code, "strong-typing")
        result.exceptions.shouldBeEmpty()
    }
}
