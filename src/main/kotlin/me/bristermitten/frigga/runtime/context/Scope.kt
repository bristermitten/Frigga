package me.bristermitten.frigga.runtime.context

import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import me.bristermitten.frigga.runtime.property.Property
import me.bristermitten.frigga.runtime.type.Type

/**
 * @author AlexL
 */
class Scope(val name: String, val insideFunction: Boolean = false)
{
	internal val properties: Multimap<String, Property> = MultimapBuilder.linkedHashKeys().linkedHashSetValues().build()
	internal val types = mutableMapOf<String, Type>()
}
