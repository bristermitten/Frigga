package me.bristermitten.frigga.runtime.expression.binary

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.entity.Function
import me.bristermitten.frigga.runtime.entity.type.AnyType
import me.bristermitten.frigga.runtime.entity.type.Type
import me.bristermitten.frigga.runtime.expression.*
import kotlin.math.pow

data class BinaryMathOperation(
    override val left: Expression,
    override val right: Expression,
    val task: (Val<*>, Val<*>) -> Expression
) : BinaryOperation {

    override val type: Type = AnyType

    override fun eval(stack: Stack, context: FriggaContext) {
        left.initialize(context)
        left.eval(stack, context)
        val pulledLeft = stack.pull()

        val leftValue = (pulledLeft as? LiteralExpression)?.value ?: pulledLeft

        right.initialize(context)
        right.eval(stack, context)
        val pulledRight = stack.pull()

        val rightValue = (pulledRight as? LiteralExpression)?.value ?: pulledRight

        val leftVal = when (leftValue) {
            is String -> {
                StringValue(leftValue)
            }
            is Double -> {
                DoubleValue(leftValue)
            }
            is Int -> {
                IntValue(leftValue)
            }
            is Function -> {
                FunctionValue(leftValue)
            }
            else -> throw UnsupportedOperationException("Left end of operator must be String, Number or Function.")
        }

        val rightVal = when (rightValue) {
            is String -> {
                StringValue(rightValue)
            }
            is Double -> {
                DoubleValue(rightValue)
            }
            is Int -> {
                IntValue(rightValue)
            }
            is Function -> {
                FunctionValue(rightValue)
            }
            else -> throw UnsupportedOperationException("Right end of operator must be String or Number. It is $rightValue")
        }

        val result = task(leftVal, rightVal)
        if (result is LiteralExpression) {
            stack.push(result.value)
        } else {
            stack.push(result)
        }
    }
}

sealed class Val<T : Val<T>>(open val value: Any, open val name: String) {
    abstract operator fun times(other: Val<*>): Expression
    abstract operator fun plus(other: Val<*>): Expression
    abstract operator fun minus(other: Val<*>): Expression
    abstract operator fun div(other: Val<*>): Expression
    abstract fun exp(other: Val<*>): Expression

    protected fun cannotPerformOperation(task: String, other: Val<*>): UnsupportedOperationException {
        return UnsupportedOperationException("Cannot $task $name and ${other.name}.")
    }
}

sealed class NumberValue<T : NumberValue<T>>(override val value: Number, override val name: String) :
    Val<T>(value, name)

data class IntValue(override val value: Int) : NumberValue<IntValue>(value, "Int") {

    override fun times(other: Val<*>): LiteralExpression {
        return when (other) {
            is StringValue -> {
                throw UnsupportedOperationException("Cannot multiply Int with String")
            }
            is IntValue -> {
                IntLiteralExpression(value * other.value)
            }
            is DoubleValue -> {
                DoubleLiteralExpression(value * other.value)
            }
            is FunctionValue -> throw cannotPerformOperation("multiply", other)
        }
    }

    override fun plus(other: Val<*>): LiteralExpression {
        return when (other) {
            is StringValue -> {
                StringLiteralExpression(value.toString() + other.value)
            }
            is IntValue -> {
                IntLiteralExpression(value + other.value)
            }
            is DoubleValue -> {
                DoubleLiteralExpression(value + other.value)
            }
            is FunctionValue -> throw cannotPerformOperation("plus", other)
        }
    }

    override fun minus(other: Val<*>): LiteralExpression {
        return when (other) {
            is StringValue -> {
                throw UnsupportedOperationException("Cannot subtract String from Int")
            }
            is IntValue -> {
                IntLiteralExpression(value - other.value)
            }
            is DoubleValue -> {
                DoubleLiteralExpression(value - other.value)
            }
            is FunctionValue -> throw cannotPerformOperation("subtract", other)
        }
    }

    override fun div(other: Val<*>): LiteralExpression {
        return when (other) {
            is StringValue -> {
                throw UnsupportedOperationException("Cannot divide Int by String")
            }
            is IntValue -> {
                IntLiteralExpression(value / other.value)
            }
            is DoubleValue -> {
                DoubleLiteralExpression(value / other.value)
            }
            is FunctionValue -> throw cannotPerformOperation("divide", other)
        }
    }

    override fun exp(other: Val<*>): LiteralExpression {
        return when (other) {
            is StringValue -> {
                throw UnsupportedOperationException("Cannot raise Int to String")
            }
            is IntValue -> {
                IntLiteralExpression(value.toDouble().pow(other.value).toInt())
            }
            is DoubleValue -> {
                DoubleLiteralExpression(value.toDouble().pow(other.value))
            }
            is FunctionValue -> throw cannotPerformOperation("exponent", other)
        }
    }
}

data class DoubleValue(override val value: Double) : NumberValue<DoubleValue>(value, "Double") {
    override fun times(other: Val<*>): LiteralExpression {
        return when (other) {
            is StringValue -> {
                throw UnsupportedOperationException("Cannot multiply Double with String")
            }
            is IntValue -> {
                DoubleLiteralExpression(value * other.value.toDouble())
            }
            is DoubleValue -> {
                DoubleLiteralExpression(value * other.value)
            }
            is FunctionValue -> throw cannotPerformOperation("multiply", other)
        }
    }

    override fun plus(other: Val<*>): LiteralExpression {
        return when (other) {
            is StringValue -> {
                StringLiteralExpression(value.toString() + other.value)
            }
            is IntValue -> {
                DoubleLiteralExpression(value + other.value)
            }
            is DoubleValue -> {
                DoubleLiteralExpression(value + other.value)
            }
            is FunctionValue -> throw cannotPerformOperation("add", other)
        }
    }

    override fun minus(other: Val<*>): LiteralExpression {
        return when (other) {
            is StringValue -> {
                throw UnsupportedOperationException("Cannot subtract String from Double")
            }
            is IntValue -> {
                DoubleLiteralExpression(value - other.value)
            }
            is DoubleValue -> {
                DoubleLiteralExpression(value - other.value)
            }
            is FunctionValue -> throw cannotPerformOperation("subtract", other)
        }
    }

    override fun div(other: Val<*>): LiteralExpression {
        return when (other) {
            is StringValue -> {
                throw UnsupportedOperationException("Cannot divide Double by String")
            }
            is IntValue -> {
                DoubleLiteralExpression(value / other.value)
            }
            is DoubleValue -> {
                DoubleLiteralExpression(value / other.value)
            }
            is FunctionValue -> throw cannotPerformOperation("divide", other)
        }
    }

    override fun exp(other: Val<*>): LiteralExpression {
        return when (other) {
            is StringValue -> {
                throw UnsupportedOperationException("Cannot raise Double to String")
            }
            is IntValue -> {
                DoubleLiteralExpression(value.pow(other.value))
            }
            is DoubleValue -> {
                DoubleLiteralExpression(value.pow(other.value))
            }
            is FunctionValue -> throw cannotPerformOperation("exponent", other)
        }
    }
}

data class StringValue(override val value: String) : Val<StringValue>(value, "String") {
    override fun times(other: Val<*>): LiteralExpression {
        return when (other) {
            is StringValue -> {
                throw UnsupportedOperationException("Cannot multiply 2 Strings")
            }
            is DoubleValue -> {
                throw UnsupportedOperationException("Cannot multiply String with Double")
            }
            is IntValue -> {
                StringLiteralExpression(value.repeat(other.value))
            }
            is FunctionValue -> throw cannotPerformOperation("multiply", other)
        }
    }

    override fun plus(other: Val<*>): LiteralExpression {
        return StringLiteralExpression(value + other.value)
    }

    override fun minus(other: Val<*>): LiteralExpression {
        throw UnsupportedOperationException("Cannot subtract from String")
    }

    override fun div(other: Val<*>): LiteralExpression {
        throw UnsupportedOperationException("Cannot divide String")
    }

    override fun exp(other: Val<*>): LiteralExpression {
        throw UnsupportedOperationException("Cannot exponent String.")
    }
}

data class FunctionValue(override val value: Function) : Val<FunctionValue>(value, "Function") {
    override fun times(other: Val<*>): Expression {
        if (other is IntValue) {
            return Function(
                value.name,
                listOf(OtherExpression(value.type) { stack, context ->
                    repeat(other.value) {
                        value.eval(stack, context)
                    }
                }),
                value.signature
            )
        }
        throw cannotPerformOperation("multiply", other)
    }

    override fun plus(other: Val<*>): Expression {
        if (other is StringValue) {
            return StringLiteralExpression(value.toString() + other.value)
        }
        throw cannotPerformOperation("add", other)
    }

    override fun minus(other: Val<*>): Expression {
        throw cannotPerformOperation("subtract", other)
    }

    override fun div(other: Val<*>): Expression {
        throw cannotPerformOperation("divide", other)
    }

    override fun exp(other: Val<*>): Expression {
        throw cannotPerformOperation("exponent", other)
    }
}
