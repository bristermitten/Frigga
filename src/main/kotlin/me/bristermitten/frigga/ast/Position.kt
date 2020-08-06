package me.bristermitten.frigga.ast

import org.antlr.v4.runtime.ParserRuleContext

/**
 * @author AlexL
 */
data class Position(val line: Int, val col: Int)

fun ParserRuleContext.getPosition() = Position(this.start.line, this.start.charPositionInLine)
