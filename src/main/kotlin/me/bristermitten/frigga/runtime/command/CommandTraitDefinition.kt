package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.PropertyDeclaration
import me.bristermitten.frigga.runtime.data.structure.Structure
import me.bristermitten.frigga.runtime.data.structure.Trait
import me.bristermitten.frigga.runtime.type.Type

data class CommandTraitDefinition(
    val name: String,
    val parents: List<Type>,
    val properties: List<PropertyDeclaration>
) : Command()
{

    override fun eval(stack: Stack, context: FriggaContext)
    {
        val struct = Trait(name, parents.mapNotNull { it.reestablish(context) as? Structure }, properties)

        context.defineType(struct)
    }

}
