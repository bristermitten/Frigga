package me.bristermitten.frigga.rewrite.function

import me.bristermitten.frigga.Position
import me.bristermitten.frigga.context.FriggaContext
import me.bristermitten.frigga.rewrite.Modifier
import me.bristermitten.frigga.rewrite.identifier.StringIdentifier
import me.bristermitten.frigga.rewrite.type.Types
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.jvmName

class JVMFunction(private val method: KFunction<*>) : Function {

    override val position: Position? = null
    override val name: String = method.name

    override val signature: FunctionSignature by lazy {
        FunctionSignature(
            method.parameters.map {
                FunctionParameter(
                    StringIdentifier(it.name ?: "NoName"),
                    Types.typeOf((it.type.classifier as KClass<*>).jvmName)
                )
            },
            Types.typeOf(method.returnType.classifier.toString())
        )
    }

    override val modifiers: Set<Modifier> = emptySet()
    override fun evaluate(context: FriggaContext): Any {
        return method.call() ?: Unit
    }
}
