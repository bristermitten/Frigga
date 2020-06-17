package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.ast.element.Modifier
import me.bristermitten.frigga.ast.element.Property
import me.bristermitten.frigga.ast.element.TypeRelationship
import me.bristermitten.frigga.ast.element.expression.value.Assignment
import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.Value

internal class CommandPropertyDefine(
    val name: String,
    val value: Assignment
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        val existing = context.findProperty(name)

        if (existing != null) {
            val redefineTo = stack.pull() as Value //Pull irrespective of mutability to ensure no leftover values on the stack
            require(Modifier.MUTABLE in existing.modifiers) {
                "Attempting to redefine a non mutable property ${existing.name}"
            }
            require(existing.value.type.accepts(redefineTo.type)) {
                "Cannot reassign ${existing.name} of type ${existing.value.type} to value of type ${redefineTo.type}"
            }
            existing.value = redefineTo.type.coerceTo(redefineTo, existing.value.type)
            return
        }

        val propertyValue = stack.pull() as Value

        val property = Property(
            name,
            value.modifiers,
            propertyValue
        )

        context.defineProperty(property)
    }
}
