package me.bristermitten.frigga.transform

import FriggaParser
import getType
import me.bristermitten.frigga.runtime.data.FriggaFile
import me.bristermitten.frigga.runtime.data.NAMESPACE_FORMAT
import me.bristermitten.frigga.runtime.data.Namespace
import me.bristermitten.frigga.runtime.data.SimpleNamespace
import me.bristermitten.frigga.runtime.data.function.Signature
import me.bristermitten.frigga.runtime.type.FunctionType
import me.bristermitten.frigga.runtime.type.NothingType
import me.bristermitten.frigga.runtime.type.Type
import me.bristermitten.frigga.transform.NodeTransformers.transform
import java.util.stream.Collectors.toList

fun FriggaParser.FriggaFileContext.load(): FriggaFile {
    return FriggaFile(
        this.namespace()?.transform(),
        this.usingList()?.use()?.transform() ?: emptySet(),
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

fun List<FriggaParser.UseContext>.transform(): Set<Namespace> {
    return emptySet()
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
