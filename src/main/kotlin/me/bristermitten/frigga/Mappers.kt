package me.bristermitten.frigga

import FriggaParser
import me.bristermitten.frigga.expression.Expression
import me.bristermitten.frigga.expression.math.*
import me.bristermitten.frigga.type.IntType
import me.bristermitten.frigga.type.Types
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token

fun FriggaParser.FriggaFileContext.toAST(considerPosition: Boolean = false) = FriggaFile(this.line().map {
    it.toAST(considerPosition)
}, toPosition(considerPosition))

fun Token.startPoint() = Point(line, charPositionInLine)
fun Token.endPoint() = Point(line, charPositionInLine + text.length)

fun FriggaParser.LineContext.toAST(considerPosition: Boolean): Statement {
    val statement = statement()
    if (statement != null) return statement.toAST(considerPosition)
    return expression().toAST(considerPosition)
}

fun ParserRuleContext.toPosition(considerPosition: Boolean): Position? {
    return if (considerPosition) Position(start.startPoint(), stop.endPoint()) else null
}

fun FriggaParser.StatementContext.toAST(considerPosition: Boolean): Statement = when (this) {
    is FriggaParser.AssignmentStatementContext -> DeclAssign(
        assignment().ID().text,
        this.assignment().typeSpec()?.ID()?.text?.let {
            Types.typeOf(it)
        } ?: IntType,
        assignment().expression().toAST(considerPosition),
        toPosition(considerPosition)
    )
//    is FriggaParser.ExpressionStatementContext -> expression().toAST()
    else -> throw UnsupportedOperationException(javaClass.name)
}

fun FriggaParser.ExpressionContext.toAST(considerPosition: Boolean): Expression =
    when (this) {
        is FriggaParser.FunctionExpressionContext -> FunctionCall(
            functionCall().ID().text,
            toPosition(considerPosition),
            functionCall().functionParams().expression().map {
                it.toAST(considerPosition)
            }
        )
        is FriggaParser.LiteralExpressionContext -> this.literal().toAST(considerPosition)
        is FriggaParser.VarReferenceContext -> VarReference(this.ID().text, toPosition(considerPosition))
        is FriggaParser.BinaryOperationContext -> toAST(considerPosition)
        is FriggaParser.FunctionDeclarationExpressionContext -> Function(
            functionDecl().line().map {
                it.toAST(considerPosition)
            },
            FunctionSignature(this.functionDecl()
                .functionSignature()
                .parameterDefinition()
                .mapIndexed { index, it ->
                    it.toParam(index)
                }),
            toPosition(considerPosition)
        )
        else -> throw UnsupportedOperationException(this.javaClass.canonicalName + this.text)
    }

fun FriggaParser.ParameterDefinitionContext.toParam(index: Int): FunctionParameter {
    val name = ID()?.text ?: "param$index"

    val type = if (typeSpec() != null) {
        typeSpec().ID().text
    } else ID().text

    return FunctionParameter(name, Types.typeOf(type))
}

fun FriggaParser.LiteralContext.toAST(considerPosition: Boolean): Expression {
    return when (this) {
        is FriggaParser.IntLiteralContext -> IntLit(INTLIT().text, toPosition(considerPosition))
        is FriggaParser.DecLiteralContext -> DecLit(DECLIT().text, toPosition(considerPosition))
//        is FriggaParser.StatementExpressionContext -> toAST(considerPosition)
        else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
    }
}

fun FriggaParser.BinaryOperationContext.toAST(considerPosition: Boolean): Expression {
    return when (operator.text) {
        "+" -> SumExpression(
            left.toAST(considerPosition),
            right.toAST(considerPosition),
            toPosition(considerPosition)
        )
        "-" -> SubtractExpression(
            left.toAST(considerPosition),
            right.toAST(considerPosition),
            toPosition(considerPosition)
        )
        "*" -> MultiplyExpression(
            left.toAST(considerPosition),
            right.toAST(considerPosition),
            toPosition(considerPosition)
        )
        "/" -> DivisionExpression(
            left.toAST(considerPosition),
            right.toAST(considerPosition),
            toPosition(considerPosition)
        )
        "^" -> PowerExpression(
            left.toAST(considerPosition),
            right.toAST(considerPosition),
            toPosition(considerPosition)
        )
        else -> throw UnsupportedOperationException(operator.text)
    }
}
