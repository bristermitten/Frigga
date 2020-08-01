package me.bristermitten.frigga.transform

import FriggaParser
import me.bristermitten.frigga.runtime.command.Command
import me.bristermitten.frigga.runtime.command.CommandInfixFunction
import me.bristermitten.frigga.runtime.command.operatorFromSymbol

//object InfixFunctionTransformer : NodeTransformer<FriggaParser.InfixFunctionContext>() {
//
//    override fun transformNode(node: FriggaParser.InfixFunctionContext): Command {
//        val left = NodeTransformers.transform(node.left)
//        val right = NodeTransformers.transform(node.right)
//        val function = node.ID().text
//
//        return CommandInfixFunction(
//            left, right,
//            operatorFromSymbol(function) ?: function
//        )
//    }
//}

