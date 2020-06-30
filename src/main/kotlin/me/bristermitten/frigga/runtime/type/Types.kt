import me.bristermitten.frigga.runtime.data.function.body
import me.bristermitten.frigga.runtime.data.function.signature
import me.bristermitten.frigga.runtime.type.*

//package me.bristermitten.frigga.runtime.type
//
//import com.google.common.collect.HashMultimap
//import com.google.common.collect.Multimap
//import me.bristermitten.frigga.runtime.FriggaContext
//import me.bristermitten.frigga.runtime.Stack
//import me.bristermitten.frigga.runtime.UPON_NAME
//import me.bristermitten.frigga.runtime.command.Command
//import me.bristermitten.frigga.runtime.data.Property
//import me.bristermitten.frigga.runtime.data.Value
//import me.bristermitten.frigga.runtime.data.function.*
//import me.bristermitten.frigga.runtime.data.function.Function
//import me.bristermitten.frigga.runtime.error.BreakException
//import me.bristermitten.frigga.util.set
//import kotlin.collections.set
//
//abstract class Type(
//    val name: String,
//    private val parent: Type? = AnyType
//) {
//
//    init {
//        @Suppress("LeakingThis")
//        types[name] = this
//    }
//
//    val staticProperty = Property(
//        name,
//        emptySet(),
//        Value(this, Unit)
//    )
//
//    private val typeFunctions: Multimap<String, Function> = HashMultimap.create()
//    private val typeProperties = mutableMapOf<String, Property>()
//    fun getFunctions(name: String): Collection<Function> = typeFunctions[name]
//    fun getProperty(name: String) = typeProperties[name]
//
//
//    open infix fun union(other: Type): Type =
//        AnyType
//

//
//    fun coerceTo(value: Value, other: Type): Value {
//        require(other.accepts(value.type)) {
//            "Cannot coerce between incompatible types ${value.type} and $other"
//        }
//        return coerceValueTo(value, other)
//    }
//
//    protected open fun coerceValueTo(value: Value, other: Type): Value =
//        Value(other, value.value)
//
//
//    protected fun defineFunction(function: Function) {
//        this.typeFunctions[function.name] = function
//        this.typeProperties[function.name] =
//            Property(function.name, emptySet(), Value(FunctionType(function.signature), function))
//    }
//
//    protected inline fun defineFunction(closure: FunctionDefiner.() -> Unit) {
//        val value = function(closure)
//        defineFunction(value)
//    }
//}
//
object StringType : Type(
    "String",
    AnyType
)

object CharType : Type(
    "CharType",
    AnyType
)
//
//object AnyType : Type("Any", null) {
//    override fun accepts(other: Type) = true
//}
//

//
internal class SimpleType(name: String) : Type(name)

//
//
object OutputType : Type("Output") {
    init {
        defineFunction {
            this.name = "println"
            signature {
                input = mapOf("value" to AnyType)
            }
            body { stack, friggaContext ->
                println(friggaContext.findProperty("value")?.value?.value)
            }
        }
    }
}

//
private val types = mutableMapOf<String, Type>()

fun loadTypes() {
    fun Type.load() = types.put(name, this)
    NumType.load()
    IntType.load()
    DecType.load()
    StringType.load()
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

