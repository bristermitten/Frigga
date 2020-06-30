package me.bristermitten.frigga.transform

import FriggaParser.*
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandValue
import me.bristermitten.frigga.runtime.data.*

object LiteralTransformer : NodeTransformer<LiteralContext>() {

    override fun transformNode(node: LiteralContext): Command {
        with(node) {
            return CommandValue(
                when (this) {
                    is IntLiteralContext -> {
                        intValue(text.toLong())
                    }
                    is DecLiteralContext -> {
                        decValue(text.toDouble())
                    }
                    is StringLiteralContext -> {
                        stringValue(this.STRING().text.removeSurrounding("\""))
                    }
                    is CharLiteralContext -> {
                        charValue(this.CHAR().text[1])
                    }
                    is BoolLiteralContext -> {
                        boolValue(this.BOOL().text!!.toBoolean())
                    }
                    else -> throw IllegalArgumentException("Unknown Literal Type $javaClass")
                }
            )
        }
    }
}
