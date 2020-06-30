package me.bristermitten.frigga.transform

import FriggaParser
import FriggaParser.UseContext
import getType
import me.bristermitten.frigga.runtime.data.*
import me.bristermitten.frigga.runtime.data.function.Signature
import me.bristermitten.frigga.runtime.type.FunctionType
import me.bristermitten.frigga.runtime.type.NothingType
import me.bristermitten.frigga.runtime.type.Type
import me.bristermitten.frigga.transform.NodeTransformers.transform
import java.util.stream.Collectors.toList

fun FriggaParser.FriggaFileContext.load(): FriggaFile {
    return FriggaFile(
        this.namespace()?.transform(),
        this.usingList()?.use()?.map(UseContext::transform)?.toSet() ?: emptySet(),
        this.body().line().stream().map {
            val exp = it.expression()
            transform(exp)
        }.collect(toList())
    )
}

fun FriggaParser.NamespaceContext.transform(): SimpleNamespace {
    val namespace = this.NAMESPACE().text
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
    throw UnsupportedOperationException(javaClass.name)
}
