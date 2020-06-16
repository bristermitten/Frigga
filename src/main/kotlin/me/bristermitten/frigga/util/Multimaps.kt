package me.bristermitten.frigga.util

import com.google.common.collect.Multimap

operator fun <K, V> Multimap<K, V>.set(k: K, v: V) = put(k, v)
