package me.bristermitten.frigga.ast

import FriggaParser
import me.bristermitten.frigga.ast.element.FriggaFile
import me.bristermitten.frigga.ast.element.Namespace
import me.bristermitten.frigga.ast.element.SimpleType
import me.bristermitten.frigga.ast.element.exp.Expression
import me.bristermitten.frigga.ast.element.exp.value.*
import me.bristermitten.frigga.ast.element.function.Function
import me.bristermitten.frigga.ast.element.function.Signature


internal fun FriggaParser.FriggaFileContext.toAST(name: String): FriggaFile {
    return FriggaFile(
        name,
        body().line().toAST(),
        headers()?.toAST() ?: emptySet()
    )
}

private fun List<FriggaParser.LineContext>.toAST(): List<Expression> {
    var previous: Expression? = null
    return mapNotNull {
        val expr = it.expression()
        val ast = expr.toAST(previous = previous)
        previous = ast
        ast
    }
}

fun FriggaParser.ExpressionContext.toAST(name: String? = null, previous: Expression? = null): Expression = when (this) {
    is FriggaParser.LiteralExpressionContext -> {
        Literal(literal().text)
    }
    is FriggaParser.FunctionExpressionContext -> {
        function().toAST(name!!)
    }
    is FriggaParser.VarReferenceContext -> {
        VarReference(text)
    }
    is FriggaParser.CallExpressionContext -> {
        Call(expression().text, call().args().expression().map { it.toAST(name, previous) })
    }
    is FriggaParser.AssignmentExpressionContext -> {
        Assignment(assignment().ID().text!!, assignment().expression().toAST(assignment().ID().text!!, previous))
    }
    is FriggaParser.AccessExpressionContext -> {
        Access(previous!!, access().expression().toAST(name, previous))
    }
    is FriggaParser.ReferencedCallExpressionContext -> {
        ReferencedCall(name!!, referencedCall().args().expression().map { it.toAST(name, previous) })
    }
    is FriggaParser.LambdaExpressionContext -> {
        lambda().toAST()
    }
    is FriggaParser.BinaryOperatorContext -> {
        BinaryOperator(left.toAST(name, previous), this.operator.text, right.toAST(name, previous))
    }
    is FriggaParser.BinaryLogicalOperatorContext -> {
        BinaryOperator(left.toAST(name, previous), this.operator.text, right.toAST(name, previous))
    }
    else -> throw UnsupportedOperationException(javaClass.simpleName)
}

internal fun FriggaParser.LambdaContext.toAST(): Lambda {

    return Lambda(
        this.lambdaParams()?.lamdaParam()?.map {
            val fullParam = it.functionParam()
            val lambdaParam: LambdaParam
            val paramName: String
            if (fullParam != null) {
                paramName = fullParam.ID().text
                lambdaParam = LambdaParam(
                    paramName,
                    SimpleType(fullParam.typeSpec().type().text)
                )
            } else {
                paramName = it.ID().text
                lambdaParam = LambdaParam(paramName)
            }

            paramName to lambdaParam
        }?.toMap() ?: emptyMap(),
        this.expression()?.let {
            listOf(it.toAST())
        } ?: block().body().line().toAST()
    )
}

internal fun FriggaParser.FunctionContext.toAST(name: String): Function {
    val signature = functionSignature()
    val typeSignature = generic()?.typeParam()
        ?.map {
            it.ID().text to SimpleType(it.typeSpec().type().text)
        }?.toMap() ?: emptyMap()

    val params = signature?.functionParams()?.functionParam()?.map {
        it.ID().text to
                SimpleType(it.typeSpec().type().text)
    }?.toMap() ?: emptyMap()

    val output = SimpleType(signature?.type()?.text ?: "Inferred Type")

    var previous: Expression? = null
    val body = block().body().line().map {
        it.expression().toAST(name, previous).apply {
            previous = this
        }
    }
    return Function(
        name,
        Signature(
            typeSignature,
            params,
            output
        ),
        body
    )
}

fun FriggaParser.HeadersContext.toAST(): Set<Namespace> {
    return use().map {
        Namespace(it.STRING().text)
    }.toSet()
}
