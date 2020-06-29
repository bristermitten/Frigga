package me.bristermitten.frigga.runtime.data

data class FriggaFile(
    val namespace: Namespace?,
    val using: Set<Namespace>,
    val content: List<CommandNode>
)
