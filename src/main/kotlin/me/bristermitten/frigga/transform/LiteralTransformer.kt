package me.bristermitten.frigga.transform

import FriggaParser.LiteralContext
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandTuple
import me.bristermitten.frigga.runtime.command.CommandValue
import me.bristermitten.frigga.runtime.data.*

object LiteralTransformer : NodeTransformer<LiteralContext>()
{

    override fun transformNode(node: LiteralContext): Command
    {
        with(node) {
            return CommandValue(
                when
                {
                    IntLiteral() != null ->
                    {
                        intValue(text.toLong())
                    }
                    DecLiteral() != null ->
                    {
                        decValue(text.toDouble())
                    }
                    StringLiteral() != null ->
                    {
                        stringValue(text.removeSurrounding("\""))
                    }
                    CharLiteral() != null ->
                    {
                        charValue(text[1])
                    }
                    BoolLiteral() != null ->
                    {
                        boolValue(text!!.toBoolean())
                    }

                    this.tupleLiteral() != null ->
                    {
                        val tuple = tupleLiteral()
                        val indexed = tuple.indexedTupleLiteral()
                        if (indexed != null)
                        {
                            val parameters = indexed.indexedTupleValues().expression()
                                .map {
                                    CommandTuple.TupleParam(null, NodeTransformers.transform(it).command)
                                }
                            return CommandTuple(parameters)
                        }
                        val named = tuple.namedTupleLiteral()
                        if (named != null)
                        {
                            val parameters = named.namedTupleValues().namedTupleValue()
                                .map {
                                    CommandTuple.TupleParam(
                                        it.ID().text,
                                        NodeTransformers.transform(it.expression()).command
                                    )
                                }
                            return CommandTuple(parameters)
                        } else
                        {
                            throw UnsupportedOperationException(tuple.text)
                        }

                    }
                    else -> throw IllegalArgumentException("Unknown Literal Type $javaClass - $text")
                }
            )
        }
    }
}
