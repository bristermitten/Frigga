package me.bristermitten.frigga.transform

import FriggaParser
import FriggaParser.UseContext
import getType
import me.bristermitten.frigga.runtime.data.*
import me.bristermitten.frigga.runtime.data.function.Signature
import me.bristermitten.frigga.runtime.type.FunctionType
import me.bristermitten.frigga.runtime.type.NothingType
import me.bristermitten.frigga.runtime.type.TupleType
import me.bristermitten.frigga.runtime.type.Type
import me.bristermitten.frigga.transform.NodeTransformers.transform

fun FriggaParser.FriggaFileContext.load(): FriggaFile {
    return FriggaFile(
        this.namespace()?.transform(),
        this.usingList()?.use()?.map(UseContext::transform)?.toSet() ?: emptySet(),
        this.body().line().map {
            val exp = it.expression()
            transform(exp)
        }
    )
}

fun FriggaParser.NamespaceContext.transform(): SimpleNamespace {
    val namespace = STRING().text.removeSurrounding("\"")
    require(NAMESPACE_FORMAT.matches(namespace)) {
        "Illegal Namespace Format $namespace"
    }

    return SimpleNamespace(namespace)
}

fun UseContext.transform(): Namespace {
    val namespace = this.STRING().text.removeSurrounding("\"")
    if (NAMESPACE_FORMAT.matches(namespace)) {
        return SimpleNamespace(namespace)
    }
    if (JVM_NAMESPACE_FORMAT.matches(namespace)) {
        return JVMNamespace(Class.forName(namespace.removePrefix("JVM:")))
    }
    throw IllegalArgumentException("Illegal namespace format $namespace")
}

fun FriggaParser.TypeContext.toType(): Type {
    val simpleType = ID()
    if (simpleType != null) {
        println("Simple Type: ${simpleType.text}")
        return getType(simpleType.text)
    }

    val nothingType = this.NOTHING()
    if (nothingType != null) {
        return NothingType
    }

    val functionType = this.functionType()
    if (functionType != null) {
        return FunctionType(
            Signature(
                emptyMap(),
                functionType.functionParamTypes().type().map {
                    val toType = it.toType()
                    toType.name to toType
                }.toMap(),
                functionType.type().toType()
            )
        )
    }
    val tupleType = this.tuple()
    if (tupleType != null) {
        val tupleTypes = tupleType.tupleParam().map {
            it.ID().text to it.typeSpec().type().toType()
        }.toMap()

        return TupleType(tupleTypes)
    }
    throw UnsupportedOperationException(javaClass.name + " " + text)
}
