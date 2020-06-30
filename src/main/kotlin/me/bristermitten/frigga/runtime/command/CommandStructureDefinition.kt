package me.bristermitten.frigga.runtime.command

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.Stack
import me.bristermitten.frigga.runtime.data.structure.Structure

data class CommandStructureDefinition(
    val defining: Structure
) : Command() {

    override fun eval(stack: Stack, context: FriggaContext) {
        context.defineType(defining)
    }
}
