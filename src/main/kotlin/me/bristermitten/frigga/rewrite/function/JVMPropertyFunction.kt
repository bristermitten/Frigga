package me.bristermitten.frigga.rewrite.function

import me.bristermitten.frigga.Position
import me.bristermitten.frigga.context.FriggaContext
import me.bristermitten.frigga.rewrite.Modifier
import me.bristermitten.frigga.rewrite.type.Types
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class JVMPropertyFunction(private val field: KProperty<*>) : Function {
    override val position: Position? = null
    override val name: String = field.name

    override val signature: FunctionSignature by lazy {
        FunctionSignature(
            emptyList(),
            Types.typeOf((field.returnType.classifier as KClass<*>).java.let {
                val clazz = if (it.isPrimitive) {
                    it.kotlin.javaObjectType
                } else it
                clazz.name
            })
        )
    }
    override val modifiers: Set<Modifier> = emptySet()
    override fun evaluate(context: FriggaContext): Any {
        return field.call() ?: Unit
    }
}
