package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.ast.element.Modifier
import me.bristermitten.frigga.ast.element.Property
import me.bristermitten.frigga.ast.element.SimpleType
import me.bristermitten.frigga.ast.element.expression.value.Assignment
import me.bristermitten.frigga.ast.element.expression.value.Literal
import me.bristermitten.frigga.scope.FriggaContext
import me.bristermitten.frigga.scope.Stack

internal class CommandPropertyDefine(
    val name: String,
    val value: Assignment
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        val existing = context.findProperty(name)
        if (existing != null) {
            require(Modifier.MUTABLE in existing.modifiers) {
                "Attempting to redefine a non mutable property ${existing.name}"
            }
            existing.value = stack.pull()
        }
        val value = stack.pull()
        val property = Property(
            name,
            (value as Literal<*>).type, //TODO
            emptySet(), //TODO
 value
        )

        println("Successfully defined $property")
        context.defineProperty(property)
    }
}
