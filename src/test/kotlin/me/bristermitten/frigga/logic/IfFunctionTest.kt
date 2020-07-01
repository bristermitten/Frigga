package me.bristermitten.frigga.logic

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.runtime.data.boolValue
import org.junit.jupiter.api.Test

class IfFunctionTest : FriggaTest() {

    @Test
    fun `Assert Correct Functionality of If Function`() {
        val code = """
            use "std"
            if(true, {
                true
            })
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe boolValue(true)
    }

    @Test
    fun `Assert Correct Functionality of If Function 2`() {
        val code = """
            use "std"
            if(false, {
                true
            })
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.shouldBeEmpty()
    }

}
