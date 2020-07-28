package me.bristermitten.frigga

import me.bristermitten.frigga.runtime.FriggaRuntime
import me.bristermitten.frigga.runtime.FullExecutionResult
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll

abstract class FriggaTest {

    @AfterEach
    fun setUp() {
        runtime.reset()
    }


    fun handleExceptions(result: FullExecutionResult) {
        result.exceptions.forEach {
            it.printStackTrace()
        }
    }

    companion object {

        @BeforeAll
        @JvmStatic
        fun setUpAll() {
            runtime = FriggaRuntime()
        }

        @JvmStatic
        lateinit var runtime: FriggaRuntime
            private set
    }
}

const val RANDOM_TEST_COUNT = 1
