package me.bristermitten.frigga.runtime.entity.type

import me.bristermitten.frigga.runtime.FriggaContext
import me.bristermitten.frigga.runtime.entity.Function
import me.bristermitten.frigga.runtime.entity.Property
import me.bristermitten.frigga.runtime.entity.Signature
import me.bristermitten.frigga.runtime.expression.OtherExpression

object NumberType : BasicType("Number")

object IntType : BasicType("Int") {
    override fun commonSuperType(other: Type): Type {
        return if (other == DoubleType) NumberType else AnyType
    }
}

object DoubleType : BasicType("Double") {
    override fun commonSuperType(other: Type): Type {
        return if (other == IntType) NumberType else AnyType
    }
}

object BoolType : BasicType("Bool")
object StringType : BasicType("String")

object PrintStreamType : BasicType("PrintStream") {
    override val properties = mapOf(
        "println" to Function(
            "println",
            listOf(
                OtherExpression(NothingType) { stack, _ ->
                    println(stack.pull())
                }
            ),
            Signature(
                NothingType,
                mapOf(
                    "value" to AnyType
                )
            )
        ).toProperty()
    )
}

fun registerBuiltInTypes(context: FriggaContext) {
    val expression = Property(
        "stdout",
        false,
        PrintStreamType,
        OtherExpression(PrintStreamType) { stack, _ ->
            stack.push(PrintStreamType)
        }
    )
    context.assign("stdout", expression)
}
