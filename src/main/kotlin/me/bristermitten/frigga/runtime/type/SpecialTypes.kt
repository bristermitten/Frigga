package me.bristermitten.frigga.runtime.type

import BoolType
import getJVMType
import me.bristermitten.frigga.runtime.CONSTRUCTOR
import me.bristermitten.frigga.runtime.THIS_NAME
import me.bristermitten.frigga.runtime.command.OPERATOR_EQUAL_NAME
import me.bristermitten.frigga.runtime.data.Value
import me.bristermitten.frigga.runtime.data.boolValue
import me.bristermitten.frigga.runtime.data.function.body
import me.bristermitten.frigga.runtime.data.function.signature
import me.bristermitten.frigga.runtime.error.BreakException
import java.lang.reflect.Modifier


object TypeType : Type("Type", null)
{
    override fun accepts(other: Type): Boolean
    {
        return other == this
    }
}

object AnyType : Type(
    "Any", null
)
{
    override fun accepts(other: Type): Boolean
    {
        return true
    }

    init
    {
        defineFunction {
            name = OPERATOR_EQUAL_NAME
            signature {
                input = mapOf("compareWith" to AnyType)
                output = BoolType
            }
            body { stack, friggaContext ->
                val compareWith = friggaContext.findParameter("compareWith")!!
                val upon = friggaContext.findProperty(THIS_NAME)!!
                val equals = upon.value == compareWith

                stack.push(boolValue(equals))
            }
        }
    }
}

data class JVMType(val jvmClass: Class<*>) : Type(jvmClass.simpleName)
{
    fun init()
    {
        jvmClass.methods.filterNot { it.isSynthetic }
            .filter { Modifier.isPublic(it.modifiers) }
            .forEach {
                defineFunction {
                    name = it.name
                    signature {
                        input = it.parameters.map { param ->
                            param.name to getJVMType(param.type)
                        }.toMap()
                        output = getJVMType(it.returnType)
                    }
                    body { stack, context ->
                        val upon = context.findProperty(THIS_NAME)!!.value
                        val result = it.invoke(upon.value, *it.parameters.map { param ->
                            val findProperty = context.findParameter(param.name)
                            findProperty?.value
                        }.toTypedArray())

                        stack.push(Value(signature.returned, result))
                    }
                }
            }

        jvmClass.constructors.forEach {
            defineFunction {
                name = CONSTRUCTOR
                signature {
                    input = it.parameters.map { param ->
                        param.name to getJVMType(param.type)
                    }.toMap()
                    output = this@JVMType
                }
                body { stack, context ->
                    val result = it.newInstance(*it.parameters.map { param ->
                        val findProperty = context.findParameter(param.name)
                        findProperty?.value
                    }.toTypedArray())

                    stack.push(Value(signature.returned, result))
                }
            }
        }
    }
}

object CallerType : Type("__Caller")
{
    init
    {
        defineFunction {
            name = "ensureInsideFunction"
            body { _, friggaContext ->
                if (!friggaContext.deepestScope.isFunctionScope)
                {
                    throw IllegalStateException("Must be called from inside a Function.")
                }
            }
        }

        defineFunction {
            name = "break"
            body { _, _ ->
                throw BreakException
            }
        }
    }
}


object NothingType : Type("Nothing", null)
{
    val INSTANCE = Unit
    override fun isSubtypeOf(other: Type): Boolean
    {
        return true
    }
}

object StackType : Type("Stack")
{
    init
    {
        defineFunction {
            name = "push"
            signature {
                input = mapOf("value" to AnyType)
            }
            body { stack, friggaContext ->
                stack.push(friggaContext.findParameter("value")!!)
            }
        }
    }
}
