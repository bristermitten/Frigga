package me.bristermitten.frigga.struct

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.runtime.data.stringValue
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class StructTests : FriggaTest() {

    @Test
    fun `Test Basic Struct Property Access Functionality`() {
        val code = """
        type = User("Alex", 16)
        type.name
        """.trimIndent()

        val result = runtime.execute(code, "structs")
        handleExceptions(result)
        result.leftoverStack.first() shouldBe stringValue("Alex")
    }

    @Test
    fun `Test Basic Struct Function Functionality`() {
        val code = """
        struct TestStruct { 
            someFunction::() -> _
        }
        
        type = TestStruct({ println("Hi") })
        type.someFunction()
        """.trimIndent()

        val result = runtime.execute(code, "structs")
        handleExceptions(result)
        result.leftoverStack.shouldBeEmpty()
    }

    @Test
    fun `Test Basic Struct Function Functionality via Reference Call`() {
        val code = """
        struct TestStruct { 
            someFunction::() -> _
        }
        
        type = TestStruct(println["Test2"])
        type.someFunction()
        """.trimIndent()

        val originalOut = System.out
        val out = ByteArrayOutputStream()
        System.setOut(PrintStream(out))

        val result = runtime.execute(code, "structs")
        handleExceptions(result)
        System.setOut(originalOut)

        out.toString() shouldBe "Test2\n"
    }
}
