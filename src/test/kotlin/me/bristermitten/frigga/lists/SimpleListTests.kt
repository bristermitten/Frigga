package me.bristermitten.frigga.lists

import me.bristermitten.frigga.FriggaTest
import org.junit.jupiter.api.Test

class SimpleListTests : FriggaTest()
{

    @Test
    fun `Assert Correct Functionality of Basic List`()
    {
        val code = """
            use std
            
            list = List()
            list.add(3)
            list.add(4)
            list.forEach((value) -> println(value))
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

//        result.leftoverStack.first() shouldBe boolValue(true)
    }
}
