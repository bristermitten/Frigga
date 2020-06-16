package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.ast.element.Named
import me.bristermitten.frigga.ast.element.Property
import me.bristermitten.frigga.ast.element.Type
import me.bristermitten.frigga.runtime.command.function.FunctionValue

class FriggaScope(override val name: String) : Named {
    internal val properties = mutableMapOf<String, Property>()
    internal val functions = mutableMapOf<String, FunctionValue>()
    internal val types = mutableMapOf<String, Type>()

    override fun toString(): String {
        return "FriggaScope(name='$name', properties=$properties, functions=$functions, types=$types)"
    }


}
