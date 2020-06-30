package me.bristermitten.frigga.runtime


const val STDOUT_NAME = "__stdout"
const val STACK_NAME = "__stack"
const val CALLER_NAME = "__caller"
const val UPON_NAME = "__upon"
const val CONSTRUCTOR = "constructor"

val reservedNames = setOf(
    STDOUT_NAME,
    STACK_NAME,
    CALLER_NAME,
    UPON_NAME
)
