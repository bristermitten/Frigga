package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.Property
import me.bristermitten.frigga.runtime.data.PropertyDeclaration
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.type.NothingType

data class CommandDeclaration(
    val declaring: PropertyDeclaration
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        val property = Property(
            declaring.name,
            declaring.modifiers,
            Value(declaring.type, NothingType.INSTANCE)
        )

        context.defineProperty(property)
        println("Defined $property")
    }

}
