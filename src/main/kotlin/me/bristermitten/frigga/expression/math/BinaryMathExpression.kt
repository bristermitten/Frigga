package me.bristermitten.frigga.expression.math

import me.bristermitten.frigga.context.FriggaContext
import me.bristermitten.frigga.Position
import me.bristermitten.frigga.expression.BinaryExpression
import me.bristermitten.frigga.expression.Expression

abstract class BinaryMathExpression(
    override val left: Expression,
    override val right: Expression,
    override val position: Position?
) : BinaryExpression {

    override fun evaluate(context: FriggaContext): Any {
        val left = left.evaluate(context)
        val right = right.evaluate(context)
        require(left is Number) {
            "Left operand of Sum operator is not a number"
        }
        require(right is Number) {
            "Right operand of Sum operator is not a number"
        }
        if (left is Double || right is Double) {
            return resolveDouble(left.toDouble(), right.toDouble())
        }
        return resolveInt(left.toInt(), right.toInt())
    }

    protected abstract fun resolveInt(left: Int, right: Int): Int
    protected abstract fun resolveDouble(left: Double, right: Double): Double
}
