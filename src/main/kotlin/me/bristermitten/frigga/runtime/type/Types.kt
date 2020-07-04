import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.THIS_NAME
import me.bristermitten.frigga.runtime.command.OPERATOR_ADD_NAME
import me.bristermitten.frigga.runtime.command.OPERATOR_NOT_NAME
import me.bristermitten.frigga.runtime.data.boolValue
import me.bristermitten.frigga.runtime.data.function.body
import me.bristermitten.frigga.runtime.data.function.signature
import me.bristermitten.frigga.runtime.data.stringValue
import me.bristermitten.frigga.runtime.type.*


object StringType : Type(
    "String",
    AnyType
) {
    init {
        defineFunction {
            name = OPERATOR_ADD_NAME
            signature {
                input = mapOf("value" to AnyType)
                output = StringType
            }
            body { stack, context ->
                val thisValue = context.findProperty(THIS_NAME)!!.value
                val addTo = context.findProperty("value")!!.value
                stack.push(stringValue((thisValue.value as String) + addTo.value))
            }
        }
    }
}

object CharType : Type(
    "Char"
) {
    init {
        defineFunction {
            name = OPERATOR_ADD_NAME
            signature {
                input = mapOf("value" to CharType)
                output = StringType
            }
            body { stack, context ->
                val thisValue = context.findProperty(THIS_NAME)!!.value
                val addTo = context.findProperty("value")!!.value
                stack.push(stringValue((thisValue.value as Char).toString() + addTo.value))
            }
        }
    }
}

object BoolType : Type(
    "Bool"
) {
    init {
        defineFunction {
            name = OPERATOR_NOT_NAME
            signature {
                output = BoolType
            }
            body { stack, friggaContext ->
                val upon = friggaContext.findProperty(THIS_NAME)!!.value
                val value = upon.value as Boolean

                stack.push(boolValue(value.not()))
            }
        }
    }
}

private class SimpleType(name: String) : Type(name) {
    override fun reestablish(context: FriggaContext): Type {
        return context.findType(name) ?: this
    }
}

object OutputType : Type("Output") {
    init {
        defineFunction {
            this.name = "println"
            signature {
                input = mapOf("value" to AnyType)
            }
            body { _, friggaContext ->
                println(friggaContext.findProperty("value")?.value?.value)
            }
        }
    }
}


private val types = mutableMapOf<String, Type>()

fun loadTypes() {
    fun Type.load() = types.put(name, this)
    NumType.load()
    IntType.load()
    DecType.load()
    StringType.load()
    CharType.load()
    BoolType.load()
    AnyType.load()
    NothingType.load()
    CallerType.load()
    OutputType.load()
}

fun getType(name: String) = types.getOrPut(name) {
    SimpleType(name)
}

fun getJVMType(jvmClass: Class<*>): Type {
    var type = JVM_EQUIVALENTS[jvmClass]
    if (type != null) {
        return type
    }
    type = types[jvmClass.name]
    if (type != null) {
        return type
    }
    type = JVMType(jvmClass)
    types[jvmClass.name] = type
    type.init()
    return type
}

val JVM_EQUIVALENTS = mapOf(
    Int::class.java to IntType,
    Long::class.java to IntType,
    Double::class.java to DecType,
    Float::class.java to DecType,
    Object::class.java to AnyType,
    Any::class.java to AnyType,
    String::class.java to StringType,
    Type::class.java to AnyType,
    Void::class.java to NothingType,
    Class::class.java to AnyType
)

