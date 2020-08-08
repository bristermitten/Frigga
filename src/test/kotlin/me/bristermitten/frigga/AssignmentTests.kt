package me.bristermitten.frigga

import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.runtime.type.DecType
import me.bristermitten.frigga.runtime.type.IntType
import me.bristermitten.frigga.runtime.value.Value
import org.junit.jupiter.api.Test

class AssignmentTests : FriggaTest()
{

	@Test
	fun `Test Simple Property Assignment`()
	{
		val code = """
            x = 3
			x
        """.trimIndent()

		val result = runtime.execute(code, javaClass.simpleName)

		result.leftoverStack.first() shouldBe Value(IntType, 3L)
	}

	@Test
	fun `Test Simple Dec Property Assignment`()
	{
		val code = """
            x = 3.5
			x
        """.trimIndent()

		val result = runtime.execute(code, javaClass.simpleName)

		result.leftoverStack.first() shouldBe Value(DecType, 3.5)
	}

	@Test
	fun `Test Simple Dec Property Assignment with Any Type`()
	{
		val code = """
            x::Any = 3.5
			x
        """.trimIndent()

		val result = runtime.execute(code, javaClass.simpleName)

		result.leftoverStack.first() shouldBe Value(DecType, 3.5)
	}
}
