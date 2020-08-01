package me.bristermitten.frigga

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.runtime.data.decValue
import me.bristermitten.frigga.runtime.data.intValue
import org.junit.jupiter.api.Test

class SimpleTests : FriggaTest()
{

    @Test
    fun `Test Simple Property Declaration`()
    {
        val code = """
            x = 3
            x
        """.trimIndent()
        val result = runtime.execute(code)

        result.leftoverStack shouldContain intValue(3)
        result.leftoverStack.shouldBeSingleton()

    }

    @Test
    fun `Test Simple Property With Explicit Type`()
    {
        val code = """
            x::Int = 3
            x
            
        """.trimIndent()
        val result = runtime.execute(code)

        result.leftoverStack shouldContain intValue(3)
        result.leftoverStack.shouldBeSingleton()
    }

    @Test
    fun `Test Immutable Property Redefinition throws Exception`()
    {
        val code = """
            x = 3
            x = 4
            x
        """.trimIndent()
        val result = runtime.execute(code)

        result.exceptions.shouldBeSingleton()
        result.leftoverStack.shouldBeEmpty()
    }

    @Test
    fun `Test Mutable Property Redefinition does not throw Exception`()
    {
        val code = """
            mutable x = 3
            x = 4
        """.trimIndent()
        val result = runtime.execute(code)

        result.exceptions.shouldBeEmpty()
        result.leftoverStack.shouldBeEmpty()
    }

    @Test
    fun `Test Property Definition and reassignment with different types`()
    {
        val code = """
            mutable x = 3.0
            x = 4
            x + 1
        """.trimIndent()
        val result = runtime.execute(code)

        handleExceptions(result)
        result.leftoverStack.shouldBeSingleton()
        result.leftoverStack.first() shouldBe decValue(5.0)
    }

    @Test
    fun `Test Property Definition and reassignment adding 1`()
    {
        val code = """
            mutable x = 3
            x = x + 1
            x
        """.trimIndent()
        val result = runtime.execute(code)

        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(4)
    }

    @Test
    fun `Test Property Definition and reassignment adding 1 twice`()
    {
        val code = """
            mutable x = 3
            x = x + 1
            x = x + 1
            x
        """.trimIndent()
        val result = runtime.execute(code)

        handleExceptions(result)
        result.leftoverStack.first() shouldBe intValue(5)
    }
}
