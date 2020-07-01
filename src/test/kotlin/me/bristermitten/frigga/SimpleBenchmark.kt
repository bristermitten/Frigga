package me.bristermitten.frigga

import io.kotest.matchers.shouldBe
import me.bristermitten.frigga.runtime.data.intValue
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class SimpleBenchmark : FriggaTest() {

    @OptIn(ExperimentalTime::class)
    @Test
    fun `Perform a simple Factorial function and get average parse time`() {
        val code = """
            use "std"
            factorial = (value::Int) -> Int {
                if(value == 1, yield[1])
                yield(value * factorial(value - 1))
            }    
            factorial(5) 
        """.trimIndent()

        val times = 100
        var totalParseTime = 0.0.toBigDecimal()
        var totalExecutionTime = 0.0.toBigDecimal()
        var totalTime = 0.0.toBigDecimal()

        repeat(times) {
            val result = runtime.execute(code)
            totalParseTime += result.timings.parseTime.toBigDecimal()
            totalExecutionTime += result.timings.runtimeTime.toBigDecimal()
            totalTime += result.timings.totalTime.toBigDecimal()

            result.leftoverStack.first() shouldBe intValue(120)
        }
        println(
            """
            ==Frigga==
            Average Parse Time = ${totalParseTime / times.toBigDecimal()} ms
            Average Execution Time = ${totalExecutionTime / times.toBigDecimal()} ms
            Average Total Time = ${totalTime / times.toBigDecimal()} ms

        """.trimIndent()
        )

        totalParseTime = 0.0.toBigDecimal()
        totalExecutionTime = 0.0.toBigDecimal()
        totalTime = 0.0.toBigDecimal()

        repeat(times) {
            val (_, time) = measureTimedValue { factorial(5) }

            totalTime += time.inMilliseconds.toBigDecimal()
        }
        println(
            """
            ==Kotlin==
            Average Execution Time = ${totalTime / times.toBigDecimal()} ms
        """.trimIndent()
        )
    }

    private fun factorial(value: Int): Int {
        if (value == 1) {
            return 1
        }
        return value * factorial(value - 1)
    }
}
