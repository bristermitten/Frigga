package me.bristermitten.frigga.transform

import FriggaParser
import FriggaParser.*
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.data.CommandNode
import org.antlr.v4.runtime.ParserRuleContext

@Suppress("UNCHECKED_CAST")
object NodeTransformers
{
    private val TRANSFORMERS = mapOf<Class<out ParserRuleContext>, NodeTransformer<*>>(
        LiteralContext::class.java to LiteralTransformer,
        LiteralExpressionContext::class.java to delegateTransformer(
            LiteralTransformer, LiteralExpressionContext::literal
        ),


        PropertyAssignmentStatementContext::class.java to delegateTransformer(
            AssignmentTransformer,
            PropertyAssignmentStatementContext::propertyAssignment
        ),
        PropertyAssignmentContext::class.java to AssignmentTransformer,

        OtherExpressionContext::class.java to object :
            NodeTransformer<OtherExpressionContext>()
        {
            override fun transformNode(node: OtherExpressionContext): Command
            {
                return transform(node.expression()).command
            }

        },

        PropertyAccessContext::class.java to PropertyAccessTransformer,

        PropertyAccessExpressionContext::class.java to delegateTransformer(
            PropertyAccessTransformer,
            PropertyAccessExpressionContext::propertyAccess
        ),

        AccessExpressionContext::class.java to AccessTransformer,
        BinaryOperatorExpressionContext::class.java to BinaryOperatorTransformer,
        ParenthesisedExpressionContext::class.java to ParenthesisExpressionTransformer,
        CallExpressionContext::class.java to CallTransformer,
        PrefixOperatorExpressionContext::class.java to PrefixOperatorTransformer,
        PropertyDeclarationStatementContext::class.java to DeclarationTransformer,
        FunctionExpressionContext::class.java to FunctionTransformer,

//        PropertyReferenceContext::class.java to PropertyReferenceTransformer,
//
//        FunctionExpressionContext::class.java to FunctionTransformer,
//
//        CallExpressionContext::class.java to CallTransformer,
//
//        AccessExpressionContext::class.java to AccessTransformer,
//
//        BinaryOperatorExpressionContext::class.java to BinaryOperatorTransformer,
//        InfixFunctionContext::class.java to InfixFunctionTransformer,
//
//        ParenthesisExpressionContext::class.java to ParenthesisExpressionTransformer,
//
//        //Structures
        StructDeclarationContext::class.java to StructDeclarationTransformer,
        StructDeclarationStatementContext::class.java to delegateTransformer(
            StructDeclarationTransformer,
            StructDeclarationStatementContext::structDeclaration
        ),

        TraitDeclarationContext::class.java to TraitDeclarationTransformer,
        TraitDeclarationStatementContext::class.java to delegateTransformer(
            TraitDeclarationTransformer,
            TraitDeclarationStatementContext::traitDeclaration
        ),

//
        LambdaExpressionContext::class.java to LambdaTransformer,
//
        ReferencedCallExpressionContext::class.java to ReferencedCallTransformer
//
//        BooleanNotContext::class.java to BooleanNotTransformer,
//
//        DeclarationExpressionContext::class.java to DeclarationTransformer,
//
//        CallableContext::class.java to object : NodeTransformer<CallableContext>() {
//            override fun transformNode(node: CallableContext): Command {
//                return transform(node.callableExpression()).command
//            }
//        }
    )

    private fun <T : ParserRuleContext> transformerFor(node: T): NodeTransformer<T>
    {
        val valueFor = getValueFor(node.javaClass)
        return valueFor as NodeTransformer<T>?
            ?: throw IllegalArgumentException("No Transformer for ${node.javaClass} ${node.text}")
    }

    private fun getValueFor(key: Class<*>): NodeTransformer<*>?
    {
        if (key == ParserRuleContext::class.java || key == Any::class.java)
        {
            return null
        }
        return TRANSFORMERS[key] ?: getValueFor(key.superclass)
    }

    fun transform(node: ParserRuleContext): CommandNode = transformerFor(node).transform(node)
}
