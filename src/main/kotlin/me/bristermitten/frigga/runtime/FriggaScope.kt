package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.runtime.data.Function
import me.bristermitten.frigga.runtime.data.Property
import me.bristermitten.frigga.runtime.type.Type

class FriggaScope(val name: String) {
    internal val properties = mutableMapOf<String, Property>()
    internal val functions = mutableMapOf<String, Function>()
    internal val types = mutableMapOf<String, Type>()

    override fun toString(): String {
        return "FriggaScope(name='$name', properties=$properties, functions=$functions, types=$types)"
    }


}
