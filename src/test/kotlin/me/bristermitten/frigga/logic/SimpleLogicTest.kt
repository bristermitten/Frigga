package me.bristermitten.frigga.logic

import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.runtime.data.boolValue
import org.junit.jupiter.api.Test

class SimpleLogicTest  : FriggaTest(){

    @Test
    fun `Assert Correct Functionality of Equals Operator`() {
        val code = """
            true == true
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe boolValue(true)
    }

}
