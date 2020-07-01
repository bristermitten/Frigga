package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.Property
import me.bristermitten.frigga.runtime.data.PropertyDeclaration
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.type.NothingType
import javax.management.openmbean.SimpleType

data class CommandDeclaration(
    val declaring: PropertyDeclaration
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        val type = declaring.type.reestablish(context)

        val property = Property(
            declaring.name,
            declaring.modifiers,
            Value(type, NothingType.INSTANCE)
        )

        context.defineProperty(property)
    }

}
