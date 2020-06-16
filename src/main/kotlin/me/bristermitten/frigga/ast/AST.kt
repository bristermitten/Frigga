package me.bristermitten.frigga.ast

import FriggaParser
import me.bristermitten.frigga.ast.element.*
import me.bristermitten.frigga.ast.element.FriggaFile
import me.bristermitten.frigga.ast.element.SimpleType
import me.bristermitten.frigga.ast.element.expression.Expression
import me.bristermitten.frigga.ast.element.expression.value.*
import me.bristermitten.frigga.ast.element.function.Function
import me.bristermitten.frigga.ast.element.function.Signature
import java.util.*

internal fun FriggaParser.FriggaFileContext.toAST(name: String): FriggaFile {
    return FriggaFile(
        name,
        body().line().toAST(),
        usingList()?.toAST() ?: emptySet()
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

fun FriggaParser.LiteralContext.toAST() = when (this) {
    is FriggaParser.IntLiteralContext -> {
        IntLiteral(INT().text.toLong())
    }
    is FriggaParser.DecLiteralContext -> {
        DecLiteral(DEC().text.toDouble())
    }
    is FriggaParser.StringLiteralContext -> {
        StringLiteral(STRING().text.removeSurrounding("\""))
    }
    else -> throw UnsupportedOperationException(javaClass.simpleName)
}

fun FriggaParser.ExpressionContext.toAST(name: String? = null, previous: Expression? = null): Expression = when (this) {
    is FriggaParser.LiteralExpressionContext -> {
        literal().toAST()
    }
    is FriggaParser.FunctionExpressionContext -> {
        function().toAST(name!!)
    }
    is FriggaParser.VarReferenceContext -> {
        PropertyReference(text)
    }
    is FriggaParser.CallExpressionContext -> {
        val expression = expression()
        val callUpon = (expression.toAST(name, previous) as? Access)?.property ?: expression.text
        Call(callUpon, call().args().expression().map { it.toAST(name, previous) })
    }
    is FriggaParser.AssignmentExpressionContext -> {
        val modifiers = this.assignment().propertyModifiers()

        val mutable = modifiers.MUTABLE()?.text
        val modifierSet = EnumSet.noneOf(Modifier::class.java)
        if (mutable != null) {
            modifierSet += Modifier.MUTABLE
        }
        val id = assignment().ID().text
        Assignment(id, modifierSet, assignment().expression().toAST(id, previous))
    }
    is FriggaParser.AccessExpressionContext -> {
        Access(access().ID().text)
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
    is FriggaParser.ParanthesisExpressionContext -> {
        this.paranthesizedExpression().expression().toAST(name, previous)
    }
    else -> throw UnsupportedOperationException(javaClass.simpleName)
}

fun FriggaParser.PropertyContext.toAST(): Expression {
    val property = ID()
    if (property != null) {
        return PropertyReference(property.text)
    }
    val call = call()
    if (call != null) {
        return Call(expression().text, call().args().expression().map { it.toAST() })
    }
    val referencedCall = referencedCall()
    if (referencedCall != null) {
        ReferencedCall(expression().text, referencedCall().args().expression().map { it.toAST() })
    }

    throw UnsupportedOperationException(this.javaClass.simpleName)
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
            it.ID().text to getType(it.typeSpec().type().text)
        }?.toMap() ?: emptyMap()

    val params = signature?.functionParams()?.functionParam()?.map {
        it.ID().text to
                getType(it.typeSpec().type().text)
    }?.toMap() ?: emptyMap()

    val output = SimpleType(signature?.type()?.text ?: "InferredType")

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

fun FriggaParser.UsingListContext.toAST(): Set<Namespace> {
    return use().map {
        Namespace(it.NAMESPACE_TEXT().text.removeSurrounding("\""))
    }.toSet()
}
