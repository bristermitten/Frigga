package me.bristermitten.frigga

interface Node {
    val position: Position?
}

data class Point(val line: Int, val column: Int)
data class Position(val start: Point, val end: Point)

