package me.bristermitten.frigga.loader

import FriggaParser
import me.bristermitten.frigga.runtime.entity.Signature
import me.bristermitten.frigga.runtime.entity.type.NothingType
import me.bristermitten.frigga.runtime.entity.type.Type
import me.bristermitten.frigga.runtime.entity.type.Types
import me.bristermitten.frigga.runtime.expression.Expression
import me.bristermitten.frigga.runtime.expression.YieldExpression

object TypeInferer {

//  fun FriggaParser.FunctionSignatureContext.inferReturnType(body: List<Expression>) : Type {
//      val yields = body.lastOrNull {
//          it is YieldExpression
//      }
//      yields?
//      return yields ?: NothingType
//  }

    fun getExplicitType(context: FriggaParser.AssignmentExpressionContext): Type? {
        val assignment = context.assignment()
        val explicitType = assignment.typeSpec()?.ID()?.text
        if (explicitType != null) {
            return Types.byName(explicitType)
        }
        return null
    }

    fun inferFunctionSignature(
        params: Map<String, Type>,
        lines: List<Expression>
    ): Signature {
        val highestYieldedType =
            lines.filterIsInstance<YieldExpression>()
                .map { it.type }
                .reduceOrNull(Type::commonSuperType) ?: NothingType

        return Signature(highestYieldedType, params)
    }
}
