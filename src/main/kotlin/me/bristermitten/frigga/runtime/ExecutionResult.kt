package me.bristermitten.frigga.runtime

data class ExecutionResult(
    val leftoverStack: List<Any>,
    val exceptions: List<Throwable> //TODO
)

data class FullExecutionResult(
    val leftoverStack: List<Any>,
    val exceptions: List<Throwable>, //TODO
    val timings: Timings
)

data class Timings(
    val lexTime: Double,
    val parseTime: Double,
    val runtimeTime: Double,
    val processTime: Double
) {
    val totalTime: Double = lexTime + parseTime + runtimeTime + processTime

    fun toPrettyString(): String {
        return """
            ========== Timings ==========
            - Lexing took $lexTime ms
            - Parsing took $parseTime ms
            - Runtime Loading took $runtimeTime ms
            - Processing took $processTime ms
            - Total Time: $totalTime ms
            =============================
        """.trimIndent()
    }
}
