package me.bristermitten.frigga.loops

import me.bristermitten.frigga.FriggaTest
import org.junit.jupiter.api.Test

class LoopTests : FriggaTest() {

    @Test
    fun `Test Basic Looping `() {
        val code = """
        use "std"

        mutable test = true
        loop(() -> test, {
            test = false
        })
        """.trimIndent()

        val result = runtime.execute(code)
        handleExceptions(result)
    }

}
