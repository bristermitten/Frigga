package me.bristermitten.frigga.runtime.data.structure

import me.bristermitten.frigga.runtime.type.Type

abstract class Structure(name: String) : Type(name) {
    abstract val parents: List<Structure>
}
