package me.bristermitten.frigga.transform

import FriggaParser
import getType
import me.bristermitten.frigga.runtime.data.*
import me.bristermitten.frigga.runtime.data.function.Signature
import me.bristermitten.frigga.runtime.type.FunctionType
import me.bristermitten.frigga.runtime.type.NothingType
import me.bristermitten.frigga.runtime.type.TupleType
import me.bristermitten.frigga.runtime.type.Type
import me.bristermitten.frigga.transform.NodeTransformers.transform

fun FriggaParser.FriggaFileContext.load(): FriggaFile
{
    val header = header()

    return FriggaFile(
        header?.getNamespace(),
        header.useDeclaration()?.map(FriggaParser.UseDeclarationContext::transform)?.toSet() ?: emptySet(),
        body().transformBody()
    )
}

fun FriggaParser.BodyContext.transformBody(): List<CommandNode>
{
    return lines().line().map { line ->
        when (line)
        {
            is FriggaParser.StatementLineContext ->
            {
                transform(line.statement())
            }
            is FriggaParser.ExpressionLineContext ->
            {
                transform(line.expression())
            }
            else ->
            {
                transform(line)
            }
        }
    }
}

fun FriggaParser.HeaderContext.getNamespace(): SimpleNamespace
{
    val namespace = this.namespaceDeclaration()?.namespaceName()?.text?.removeSurrounding("\"")
    namespace ?: return SimpleNamespace("")
    require(NAMESPACE_FORMAT.matches(namespace)) {
        "Illegal Namespace Format $namespace"
    }

    return SimpleNamespace(namespace)
}

fun FriggaParser.UseDeclarationContext.transform(): Namespace
{
    val namespace = this.useNamespaceName()

    val simpleNamespace = namespace.namespaceName()
    if (simpleNamespace != null)
    {
        return SimpleNamespace(simpleNamespace.text.removeSurrounding("\""))
    }

    val jvm = namespace.javaPackageName()
    if (jvm != null)
    {
        return JVMNamespace(Class.forName(jvm.text.removeSurrounding("\"")))
    }

    throw IllegalArgumentException("Illegal namespace format ${namespace.text}")
}

fun FriggaParser.TypeContext.toType(): Type
{
    val simple = structType()
    if (simple != null)
    {
        return getType(simple.ID().text)
    }
    val nothing = nothingType()
    if (nothing != null)
    {
        return NothingType
    }

    val functionType = functionType()
    if (functionType != null)
    {
        return FunctionType(
            Signature(
                emptyMap(),
                functionType.functionTypeParameterTypes().type().map {
                    val toType = it.toType()
                    toType.name to toType
                }.toMap(),
                functionType.functionTypeReturnType().type().toType()
            )
        )
    }
    val tupleType = tupleType()
    if (tupleType != null)
    {
        val tupleTypes = tupleType.tupleTypeParam().map {
            it.ID().text to it.type().toType()
        }.toMap()

        return TupleType(tupleTypes)
    }

    throw UnsupportedOperationException(this.text)
}
