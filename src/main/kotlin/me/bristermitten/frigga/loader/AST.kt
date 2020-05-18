package me.bristermitten.frigga.loader

import FriggaParser
import FriggaParser.FriggaFileContext
import FriggaParser.LineContext
import me.bristermitten.frigga.loader.TypeInferer.inferFunctionSignature
import me.bristermitten.frigga.runtime.FriggaFile
import me.bristermitten.frigga.runtime.entity.Function
import me.bristermitten.frigga.runtime.entity.type.Types
import me.bristermitten.frigga.runtime.expression.*
import me.bristermitten.frigga.runtime.expression.binary.BinaryMathOperation
import me.bristermitten.frigga.runtime.expression.binary.Val
import me.bristermitten.frigga.util.escape

fun FriggaFileContext.toAST(): FriggaFile = FriggaFile(line().map(LineContext::toAST))

fun LineContext.toAST(): Expression = expression().toAST()

fun FriggaParser.ExpressionContext.toAST(name: String? = null, inFunction: Boolean = false): Expression {
    return when (this) {
        is FriggaParser.VarReferenceContext -> {
            if (inFunction) {
                ParameterReferenceExpression(ID().text)
            } else {
                VarReferenceExpression(ID().text)
            }
        }
        is FriggaParser.AssignmentExpressionContext -> {
            val assignment = assignment()
            val assignmentName = assignment.ID().text

            val expression = assignment.expression().toAST(assignmentName, inFunction)
            AssignmentExpression(
                assignmentName,
                expression,
                TypeInferer.getExplicitType(this)
            )
        }
        is FriggaParser.LiteralExpressionContext -> {
            literal().toAST()
        }
        is FriggaParser.FunctionDeclarationExpressionContext -> {
            val declaration = functionDecl()

            val paramTypes = declaration.functionSignature()?.parameterDefinition()?.map {
                it.ID().text to Types.byName(it.typeSpec().ID().text)
            }?.toMap() ?: emptyMap()

            val lines = declaration.line().map { it.expression().toAST(name, true) }
            Function(
                name = name ?: "Anonymous$1",
                body = lines,
                signature = inferFunctionSignature(paramTypes, lines)
            )
        }
        is FriggaParser.FunctionCallExpressionContext -> {
            val functionCall = functionCall()
            FunctionCallExpression(
                functionCall.objectRef()?.ID()?.text,
                functionCall.ID().text,
                functionCall.functionParams().expression().map { it.toAST(name, inFunction) }
            )
        }
        is FriggaParser.BinaryOperationContext -> {
            toAST()
        }
        is FriggaParser.InverseOperationContext -> {
            InverseOperationExpression(expression().toAST(name, inFunction))
        }
        else -> throw UnsupportedOperationException(javaClass.name + " " + text)
    }
}

fun FriggaParser.LiteralContext.toAST(): Expression = when (this) {
    is FriggaParser.IntLiteralContext -> {
        IntLiteralExpression(text.toInt())
    }
    is FriggaParser.DecLiteralContext -> {
        DoubleLiteralExpression(text.toDouble())
    }
    is FriggaParser.BoolLiteralContext -> {
        BoolLiteralExpression(text!!.toBoolean())
    }
    is FriggaParser.StringLiteralContext -> {
        StringLiteralExpression(
            STRING().text
                .drop(1).dropLast(1) //Remove quotations
                .escape()
        )
    }
    else -> throw UnsupportedOperationException(javaClass.name + " " + text)
}

fun FriggaParser.BinaryOperationContext.toAST(): Expression {
    return when (operator.text) {
        "+" -> BinaryMathOperation(left.toAST(), right.toAST(), Val<*>::plus)
        "-" -> BinaryMathOperation(left.toAST(), right.toAST(), Val<*>::minus)
        "*" -> BinaryMathOperation(left.toAST(), right.toAST(), Val<*>::times)
        "/" -> BinaryMathOperation(left.toAST(), right.toAST(), Val<*>::div)
        "^" -> BinaryMathOperation(left.toAST(), right.toAST(), Val<*>::exp)
        else -> throw UnsupportedOperationException(javaClass.name + " " + operator.text)
    }
}
