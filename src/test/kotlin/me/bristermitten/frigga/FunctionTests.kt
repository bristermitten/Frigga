package me.bristermitten.frigga

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.runtime.data.intValue
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class FunctionTests : FriggaTest() {

    @Test
    fun `Test correct handling of a No-Op Function`() {
        val code = """
            doNothing = () -> _ {
            
            }
            doNothing()
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)
        result.leftoverStack.shouldBeEmpty()
    }

    @Test
    fun `Test correct handling of a No-Op Function with an explicit Type`() {
        val code = """
            doNothing::() -> _ = () -> _ {
            
            }
            doNothing()
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)
        result.leftoverStack.shouldBeEmpty()
    }

    @Test
    fun `Test correct handling of a Simple Function that yields a value`() {
        val code = """
            getValue = () -> Int {
                yield(3)
            }
            getValue()
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)
        result.leftoverStack shouldContain intValue(3)
    }
    @Test
    fun `Test correct handling of a nested call`() {
        val code = """
            getANumber = () -> Int {
                yield(10)
            }
            getValue = () -> Int {
                yield(getANumber())
            }
            getValue()
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)
        result.leftoverStack shouldContain intValue(10)
    }

    @ExperimentalStdlibApi
    @Test
    fun `Test yield function correctly breaking out of execution`() {
        val code = """
            getValue = () -> Int {
                yield(3)
                println("Hello")
            }
            getValue()
        """.trimIndent()

        val originalOut = System.out
        val out = ByteArrayOutputStream()
        System.setOut(PrintStream(out))

        val result = runtime.execute(code)
        handleExceptions(result)
        System.setOut(originalOut)

        out.size() shouldBe 0
    }
}
