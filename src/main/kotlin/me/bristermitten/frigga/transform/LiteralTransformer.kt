package me.bristermitten.frigga.transform

import FriggaParser.*
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandValue
import me.bristermitten.frigga.runtime.data.charValue
import me.bristermitten.frigga.runtime.data.decValue
import me.bristermitten.frigga.runtime.data.intValue
import me.bristermitten.frigga.runtime.data.stringValue

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
                    else -> throw IllegalArgumentException("Unknown Literal Type $javaClass")
                }
            )
        }
    }
}
