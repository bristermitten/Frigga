package me.bristermitten.frigga

import me.bristermitten.frigga.runtime.FriggaRuntime
import me.bristermitten.frigga.runtime.FullExecutionResult
import org.junit.jupiter.api.BeforeEach

abstract class FriggaTest {
    protected lateinit var runtime: FriggaRuntime
        private set

    @BeforeEach
    fun setUp() {
        runtime = FriggaRuntime()
    }


    fun handleExceptions(result: FullExecutionResult) {
        result.exceptions.forEach {
            throw it
        }
    }
}

const val RANDOM_TEST_COUNT = 50
