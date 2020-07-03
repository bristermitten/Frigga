package me.bristermitten.frigga.runtime


const val STDOUT_NAME = "__stdout"
const val STACK_NAME = "__stack"
const val CALLER_NAME = "__caller"
const val UPON_NAME = "__upon"
const val IF_NAME = "if"
const val CONSTRUCTOR = "constructor"
const val STD_NAMESPACE = "std"

val reservedNames = setOf(
    STDOUT_NAME,
    STACK_NAME,
    CALLER_NAME,
    UPON_NAME
)
