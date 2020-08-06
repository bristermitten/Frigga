package me.bristermitten.frigga

import org.junit.jupiter.api.Test

class SimpleTests : FriggaTest()
{

	@Test
	fun `Test Simple Property Declaration`()
	{
		val code = """
            x = 3
        """.trimIndent()
		val result = runtime.execute(code, javaClass.simpleName)

	}

}
