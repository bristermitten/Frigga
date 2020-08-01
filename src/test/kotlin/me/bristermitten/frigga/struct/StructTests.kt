package me.bristermitten.frigga.struct

import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.FriggaTest
import me.bristermitten.frigga.runtime.data.intValue
import me.bristermitten.frigga.runtime.data.stringValue
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class StructTests : FriggaTest() {

    @Test
    fun `Test Basic Struct Property Access Functionality`() {
        val code = """
        struct User {
            name::String
            mutable age::Int
        }
        type = User("Alex", 16)
        type.name
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)
        result.leftoverStack.first() shouldBe stringValue("Alex")
    }

    @Test
    fun `Test Basic Struct Function Functionality`() {
        val code = """
        use std
        
        struct TestStruct { 
            someFunction::() -> _
        }
        
        type = TestStruct({ println("Hi") })
        type.someFunction()
        """.trimIndent()

        val originalOut = System.out
        val out = ByteArrayOutputStream()
        System.setOut(PrintStream(out))

        val result = runtime.execute(code)
        handleExceptions(result)

        System.setOut(originalOut)

        out.toString() shouldBe "Hi" + System.lineSeparator()
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

        val result = runtime.execute(code)
        handleExceptions(result)
        System.setOut(originalOut)

        out.toString() shouldBe "Test2" + System.lineSeparator()
    }

    @Test
    fun `Test Basic Struct Function Functionality Returning a Value`() {
        val code = """
        struct TestStruct { 
            getANumber::() -> Int
        }
        
        type = TestStruct(() -> 3)
        type.getANumber()
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe intValue(3)
    }

    @Test
    fun `Test Struct Function Functionality of a Function using a property`() {
        val code = """
        struct TestStruct {
            age::Int
            getANumber::() -> Int
        }
        
        type = TestStruct(3, () -> this.age)
        type.getANumber()
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)

        result.leftoverStack.first() shouldBe intValue(3)
    }

}
