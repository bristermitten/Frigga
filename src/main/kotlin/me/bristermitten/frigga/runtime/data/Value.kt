package me.bristermitten.frigga.runtime.data

import BoolType
import CharType
import StringType
import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.type.*


data class Value(val type: Type, val value: Any)
{
	override fun toString(): String
	{
		return value.toString()
	}
}

fun Value.establishType(context: FriggaContext): Type
{
	val type = if (type == TypeType)
	{
		value as Type
	} else
	{
		type
	}
	return type.reestablish(context)
}

fun intValue(value: Long) = Value(IntType, value)
fun decValue(value: Double) = Value(DecType, value)
fun stringValue(value: String) = Value(StringType, value)
fun charValue(value: Char) = Value(CharType, value)
fun boolValue(value: Boolean) = Value(BoolType, value)

fun indexedTupleValue(values: List<Value>) = Value(
	IndexedTupleType(values.map { it.type }), values
)

fun namedTupleValue(values: Map<String, Value>) = Value(
	NamedTupleType(
		values.mapValues { it.value.type }
	), values
)

