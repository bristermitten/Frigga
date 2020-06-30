package me.bristermitten.frigga.runtime.command


const val OPERATOR_ADD_NAME = "add"
const val OPERATOR_ADD = "+"

const val OPERATOR_TAKE_NAME = "take"
const val OPERATOR_TAKE = "-"

const val OPERATOR_TIMES_NAME = "times"
const val OPERATOR_TIMES = "*"

const val OPERATOR_DIVIDE_NAME = "divide"
const val OPERATOR_DIVIDE = "/"

const val OPERATOR_EXPONENT_NAME = "exp"
const val OPERATOR_EXPONENT = "^"

const val OPERATOR_NOT_NAME = "not"
const val OPERATOR_NOT = "!"


fun operatorFromSymbol(symbol: String): String? {
    return when (symbol) {
        OPERATOR_ADD -> OPERATOR_ADD_NAME
        OPERATOR_TAKE -> OPERATOR_TAKE_NAME
        OPERATOR_TIMES -> OPERATOR_TIMES_NAME
        OPERATOR_DIVIDE -> OPERATOR_DIVIDE_NAME
        OPERATOR_EXPONENT -> OPERATOR_EXPONENT_NAME
        else -> null
    }
}
