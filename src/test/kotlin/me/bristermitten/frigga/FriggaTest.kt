package me.bristermitten.frigga

import me.bristermitten.frigga.scope.FriggaRuntime
import org.junit.jupiter.api.BeforeEach

abstract class FriggaTest {
    protected lateinit var runtime: FriggaRuntime
        private set

    @BeforeEach
    fun setUp() {
        runtime = FriggaRuntime()
    }
}
