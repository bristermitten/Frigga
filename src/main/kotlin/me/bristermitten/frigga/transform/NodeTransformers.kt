package me.bristermitten.frigga.transform

import FriggaParser.*
import me.bristermitten.frigga.runtime.data.CommandNode
import org.antlr.v4.runtime.ParserRuleContext

@Suppress("UNCHECKED_CAST")
object NodeTransformers {
    private val TRANSFORMERS = mapOf<Class<out ParserRuleContext>, NodeTransformer<*>>(
        LiteralContext::class.java to LiteralTransformer,

        LiteralExpressionContext::class.java to delegateTransformer(
            LiteralTransformer, LiteralExpressionContext::literal
        ),

        AssignmentExpressionContext::class.java to AssignmentTransformer,

        PropertyReferenceContext::class.java to PropertyReferenceTransformer,

        FunctionExpressionContext::class.java to FunctionTransformer,

        CallExpressionContext::class.java to CallTransformer,

        AccessExpressionContext::class.java to AccessTransformer,

        BinaryOperatorExpressionContext::class.java to BinaryOperatorTransformer,
        InfixFunctionContext::class.java to InfixFunctionTransformer,

        ParenthesisExpressionContext::class.java to ParenthesisExpressionTransformer,

        //Structures
        StructureDefinitionContext::class.java to delegateTransformer(
            StructureDefinitionTransformer,
            StructureDefinitionContext::structureDef
        ),

        LambdaExpressionContext::class.java to LambdaTransformer,

        ReferencedCallExpressionContext::class.java to ReferencedCallTransformer,

        BooleanNotContext::class.java to BooleanNotTransformer,

        DeclarationExpressionContext::class.java to DeclarationTransformer
    )

    private fun <T : ParserRuleContext> transformerFor(node: T): NodeTransformer<T> {
        val valueFor = getValueFor(node.javaClass)
        return valueFor as NodeTransformer<T>?
            ?: throw IllegalArgumentException("No Transformer for ${node.javaClass} ${node.text}")
    }

    private fun getValueFor(key: Class<*>): NodeTransformer<*>? {
        if (key == ParserRuleContext::class.java || key == Any::class.java) {
            return null
        }
        return TRANSFORMERS[key] ?: getValueFor(key.superclass)
    }

    fun transform(node: ParserRuleContext): CommandNode = transformerFor(node).transform(node)
}
