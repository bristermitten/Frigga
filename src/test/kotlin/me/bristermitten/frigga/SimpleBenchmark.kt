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
            factorial(8) 
        """.trimIndent()

        val times = 1000
        var totalParseTime = 0.0.toBigDecimal()
        var totalExecutionTime = 0.0.toBigDecimal()
        var totalTime = 0.0.toBigDecimal()

        repeat(times) {
            val result = runtime.execute(code)
            totalParseTime += result.timings.parseTime.toBigDecimal()
            totalExecutionTime += result.timings.runtimeTime.toBigDecimal()
            totalTime += result.timings.totalTime.toBigDecimal()

            result.leftoverStack.first() shouldBe intValue(factorial(8))
        }

        val averageFriggaExecutionTime = totalExecutionTime / times.toBigDecimal()
        println(
            """
            ** Calculation of 8! **
            ==Frigga==
            Average Parse Time = ${totalParseTime / times.toBigDecimal()} ms
            Average Execution Time = $averageFriggaExecutionTime ms
            Average Total Time = ${totalTime / times.toBigDecimal()} ms

        """.trimIndent()
        )

        totalParseTime = 0.0.toBigDecimal()
        totalExecutionTime = 0.0.toBigDecimal()
        totalTime = 0.0.toBigDecimal()

        repeat(times) {
            val (_, time) = measureTimedValue { factorial(8) }

            totalTime += time.inMilliseconds.toBigDecimal()
        }
        println("""
            ==Kotlin==
            Average Execution Time = ${totalTime / times.toBigDecimal()} ms
            
            Therefore, JVM Compiled Kotlin is about ${averageFriggaExecutionTime / (totalTime / times.toBigDecimal())}x faster than interpreted Frigga.
        """.trimIndent())
    }

    private fun factorial(value: Int): Long {
        if (value == 1) {
            return 1
        }
        return value * factorial(value - 1)
    }
}
