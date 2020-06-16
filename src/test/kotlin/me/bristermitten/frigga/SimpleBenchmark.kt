package me.bristermitten.frigga

import org.junit.jupiter.api.Test

class SimpleBenchmark : FriggaTest() {

    @Test
    fun `Perform 1_000_000 tests and get average parse time`() {
        val code = """
            x = 3
            x
        """.trimIndent()

        val times = 1_000_000
        var totalParseTime = 0.0
        var totalExecutionTime = 0.0

        repeat(times) {
            val result = runtime.execute(code, "benchmark")
            totalParseTime += result.timings.parseTime
            totalExecutionTime += result.timings.totalTime
            runtime.reset()
        }
        println("Average Parse Time = ${totalParseTime / times} ms")
        println("Average Total Execution Time = ${totalExecutionTime / times} ms")
    }
}
