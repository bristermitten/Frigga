package me.bristermitten.frigga.rewrite.type

import me.bristermitten.frigga.rewrite.function.Function
import me.bristermitten.frigga.rewrite.function.JVMFunction
import me.bristermitten.frigga.rewrite.function.JVMPropertyFunction
import me.bristermitten.frigga.rewrite.identifier.Identifier
import me.bristermitten.frigga.rewrite.identifier.StringIdentifier
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

class JVMType(clazz: Class<*>) : Type {
    override val name: String = clazz.simpleName

    override val members: Map<Identifier, Function> = (clazz.kotlin.members)
        .map {
            StringIdentifier(it.name) to
                    when (it) {
                        is KProperty -> {
                            JVMPropertyFunction(it)
                        }
                        is KFunction -> {
                            JVMFunction(it)
                        }
                        else -> throw UnsupportedOperationException(it.javaClass.simpleName)
                    }
        }.toMap()

    override fun toString(): String {
        return "JVMType(name='$name', members=$members)"
    }


}
