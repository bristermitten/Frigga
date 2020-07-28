package me.bristermitten.frigga.runtime.data

data class Position(
    val line: Int,
    val column: Int
) {
    companion object {
        val INVALID = Position(-1, -1)
    }
}
