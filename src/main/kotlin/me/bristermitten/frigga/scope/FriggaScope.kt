package me.bristermitten.frigga.scope

import me.bristermitten.frigga.ast.element.Named
import me.bristermitten.frigga.ast.element.Property
import me.bristermitten.frigga.ast.element.Type
import me.bristermitten.frigga.ast.element.function.Function

class FriggaScope(override val name: String) : Named {
    internal val properties = mutableMapOf<String, Property>()
    internal val functions = mutableMapOf<String, Function>()
    internal val types = mutableMapOf<String, Type>()

    override fun toString(): String {
        return "FriggaScope(name='$name', properties=$properties, functions=$functions, types=$types)"
    }


}
