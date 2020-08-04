package me.bristermitten.frigga.runtime

import com.google.common.collect.HashMultimap
import me.bristermitten.frigga.runtime.data.Property
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.type.Type

class FriggaScope(val name: String, val isFunctionScope: Boolean = false)
{
    internal val properties = HashMultimap.create<String, Property>(1, 1)
    internal val types = mutableMapOf<String, Type>()

    internal val parameters = mutableMapOf<String, Value>()
    internal val typeParameters = mutableMapOf<String, Type>()

    override fun toString(): String
    {
        return "FriggaScope(name='$name', isFunctionScope=$isFunctionScope, properties=$properties, types=$types, parameters=$parameters)"
    }


}
