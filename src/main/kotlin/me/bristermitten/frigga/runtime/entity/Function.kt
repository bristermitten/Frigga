package me.bristermitten.frigga.runtime.entity

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Scope
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.entity.type.Type
import me.bristermitten.frigga.runtime.expression.Expression

data class Function(
    val name: String,
    val body: List<Expression>,
    val signature: Signature
) : Expression {

    override fun eval(stack: Stack, context: FriggaContext) {
        context.enterScope(Scope(name))

        body.forEach {
            it.eval(stack, context)
        }

        context.exitScope()
    }


    override val type: Type = signature

    override fun toString(): String {
        return buildString {
            append("Function {\n")

            append("    Name: ")
            append(name)
            append('\n')

            append("    Signature: ")
            append(signature)
            append('\n')

            append("    Body {\n")
            for (line in body) {
                append("      ")
                append(line)
                append('\n')
            }
            append("    }")
            append("    \n")

            append("  }")
        }
    }

    fun toProperty() = Property(
        this.name,
        false,
        this.signature.returnType,
        this
    )
}
