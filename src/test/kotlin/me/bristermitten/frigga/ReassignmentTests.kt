package me.bristermitten.frigga

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.runtime.type.DecType
import me.bristermitten.frigga.runtime.type.IntType
import me.bristermitten.frigga.runtime.value.Value
import org.junit.jupiter.api.Test

class ReassignmentTests : FriggaTest()
{

	@Test
	fun `Test Immutable Property Reassignment`()
	{
		val code = """
            x = 3
			x = 4
        """.trimIndent()

		shouldThrow<Exception> {
			runtime.execute(code, javaClass.simpleName)
		}
	}

	@Test
	fun `Test Mutable Property Reassignment`()
	{
		val code = """
            mutable x = 3.5
			x = 3.4
        """.trimIndent()

		shouldNotThrowAny {
			runtime.execute(code, javaClass.simpleName)
		}
	}

	@Test
	fun `Test Mutable Property Reassignment with wrong type`()
	{
		val code = """
            mutable x = 3.5
			x = "hello"
        """.trimIndent()

		shouldThrowAny {
			runtime.execute(code, javaClass.simpleName)
		}
	}
}
