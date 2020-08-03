package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.indexedTupleValue
import me.bristermitten.frigga.runtime.data.namedTupleValue

/**
 * @author AlexL
 */
class CommandTuple(
    val parameters: List<TupleParam>
) : Command()
{
    data class TupleParam(val name: String?, val expression: Command)


    override fun eval(stack: Stack, context: FriggaContext)
    {
        val named = parameters.any { it.name != null }

        if (named)
        {
            val value = namedTupleValue(parameters.map {
                it.name!! to { it.expression.eval(stack, context); stack.pull() }()
            }.toMap())

            stack.push(value)

        } else
        {
            val value = indexedTupleValue(
                parameters.map {
                    it.expression.eval(stack, context)
                    stack.pull()
                }
            )
            stack.push(value)
        }
    }
}
