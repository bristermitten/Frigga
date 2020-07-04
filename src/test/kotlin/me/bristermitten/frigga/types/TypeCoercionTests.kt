package me.bristermitten.frigga.types

import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.runtime.data.intValue
import org.junit.jupiter.api.Test

class TypeCoercionTests : FriggaTest() {

    @Test
    fun `Assert that Int can be substituted for Int Producer Function`() {
        val code = """
            takeInt = (producer::() -> Int) -> _ {
                producer()
            }
            takeInt(3)
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe intValue(3)
    }
}
