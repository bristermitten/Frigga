package me.bristermitten.frigga

data class FriggaFile(val statements: List<Statement>, override val position: Position?) : Node
