package me.bristermitten.frigga.runtime.data

import me.bristermitten.frigga.runtime.command.Command

data class CommandNode(
    val command: Command,
    val position: Position
)
