package me.bristermitten.frigga.runtime.data.structure

import me.bristermitten.frigga.runtime.CONSTRUCTOR
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.data.function.body
import me.bristermitten.frigga.runtime.data.function.signature
import me.bristermitten.frigga.runtime.type.TypeInstance
import me.bristermitten.frigga.runtime.type.TypeProperty

class Struct(
    name: String,
    override val parents: List<Structure>,
    val elements: List<TypeProperty>
) : Structure(name) {
    init {
        defineFunction {
            this.name = CONSTRUCTOR
            signature {
                output = this@Struct
                input = elements.map { it.name to it.type }.toMap()
            }
            body { stack, friggaContext ->
                val newInstance = TypeInstance(
                    this@Struct,
                    elements.map {
                        it to friggaContext.findProperty(it.name)!!.value
                    }.toMap()
                )

                stack.push(Value(this@Struct, newInstance))
            }
        }

        elements.forEach(this::defineProperty)
    }
}
