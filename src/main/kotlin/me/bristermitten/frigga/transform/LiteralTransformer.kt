package me.bristermitten.frigga.transform

import FriggaParser.LiteralContext
import me.bristermitten.frigga.runtime.command.Command
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
                        stringValue(this.text.removeSurrounding("\""))
                    }
                    CharLiteral() != null ->
                    {
                        charValue(this.text[1])
                    }
                    BoolLiteral() != null ->
                    {
                        boolValue(this.text!!.toBoolean())
                    }
                    else -> throw IllegalArgumentException("Unknown Literal Type $javaClass")
                }
            )
        }
    }
}
