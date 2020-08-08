package me.bristermitten.frigga.util

import com.google.common.collect.Multimap

/**
 * @author AlexL
 */

operator fun <K, V> Multimap<K, V>.set(key: K, value: V)
{
	put(key, value)
}
