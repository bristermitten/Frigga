package me.bristermitten.frigga.expression.math

import me.bristermitten.frigga.Position
import me.bristermitten.frigga.expression.Expression
import kotlin.math.pow
import kotlin.math.roundToInt

data class SumExpression(
    override val left: Expression,
    override val right: Expression,
    override val position: Position?
) : BinaryMathExpression(left, right, position) {

    override fun resolveInt(left: Int, right: Int): Int {
        return left + right
    }

    override fun resolveDouble(left: Double, right: Double): Double {
        return left + right
    }
}

data class SubtractExpression(
    override val left: Expression,
    override val right: Expression,
    override val position: Position?
) : BinaryMathExpression(left, right, position) {

    override fun resolveInt(left: Int, right: Int): Int {
        return left - right
    }

    override fun resolveDouble(left: Double, right: Double): Double {
        return left - right
    }
}

data class MultiplyExpression(
    override val left: Expression,
    override val right: Expression,
    override val position: Position?
) : BinaryMathExpression(left, right, position) {

    override fun resolveInt(left: Int, right: Int): Int {
        return left * right
    }

    override fun resolveDouble(left: Double, right: Double): Double {
        return left * right
    }
}

data class DivisionExpression(
    override val left: Expression,
    override val right: Expression,
    override val position: Position?
) : BinaryMathExpression(left, right, position) {

    override fun resolveInt(left: Int, right: Int): Int {
        return left / right
    }

    override fun resolveDouble(left: Double, right: Double): Double {
        return left / right
    }
}

data class PowerExpression(
    override val left: Expression,
    override val right: Expression,
    override val position: Position?
) : BinaryMathExpression(left, right, position) {

    override fun resolveInt(left: Int, right: Int): Int {
        return left.toDouble().pow(right.toDouble()).roundToInt()
    }

    override fun resolveDouble(left: Double, right: Double): Double {
        return left.pow(right)
    }
}
