package me.bristermitten.frigga.math

import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.runtime.decValue
import me.bristermitten.frigga.runtime.intValue
import org.junit.jupiter.api.Test

class SquareRootTests : FriggaTest() {
    @Test
    fun `Assert Simple Integer sqrt Functioning Correctly`() {
        val code = """
            use "std"
            x = 16
            square_root(x)
        """.trimIndent()

        val result = runtime.execute(code, "math")
        handleExceptions(result)
        result.leftoverStack.first() shouldBe decValue(4.0)

        //TODO implement
    }
}
