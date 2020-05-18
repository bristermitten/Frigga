package me.bristermitten.frigga.util

fun String.escape() =
    this.replace("\\\\", "\\")
        .replace("\\t", "\t")
        .replace("\\b", "\b")
        .replace("\\n", "\n")
        .replace("\\r", "\r")
        .replace("\\'", "\'")
        .replace("\\\"", "\"")
