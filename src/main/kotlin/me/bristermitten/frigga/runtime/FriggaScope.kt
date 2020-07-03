package me.bristermitten.frigga.runtime

import me.bristermitten.frigga.runtime.data.Property
import me.bristermitten.frigga.runtime.data.function.Function
import me.bristermitten.frigga.runtime.type.Type

class FriggaScope(val name: String, val isFunctionScope: Boolean = false) {
    internal val properties = mutableMapOf<String, Property>()
    internal val types = mutableMapOf<String, Type>()

    override fun toString(): String {
        return "FriggaScope(name='$name', properties=$properties, types=$types)"
    }


}
