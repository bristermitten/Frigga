package me.bristermitten.frigga

import me.bristermitten.frigga.runtime.FriggaRuntime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll

abstract class FriggaTest {

    @AfterEach
    fun setUp() {
//        runtime.reset()
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

const val RANDOM_TEST_COUNT = 5
