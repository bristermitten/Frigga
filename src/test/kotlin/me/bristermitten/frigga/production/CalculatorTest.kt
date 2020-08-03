package me.bristermitten.frigga.production

import me.bristermitten.frigga.runtime.FriggaRuntime

/**
 * @author AlexL
 */

fun main()
{
    val code = """
            use std
            use io

            println("Enter First Number")
            first = readln().toInt()
            
            println("Enter Second Number")
            second = readln().toInt()

            println("The Result is: " + (first + second))

        """.trimIndent()

    val runtime = FriggaRuntime()
    val result = runtime.execute(code)

    result.exceptions.forEach { throw it }

}
