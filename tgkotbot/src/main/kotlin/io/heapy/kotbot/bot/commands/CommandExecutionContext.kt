package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.model.Message

data class CommandExecutionContext(
    val command: Command,
    val message: Message,
    val currentContext: Command.Context,
    val currentAccess: Command.Access,
)
