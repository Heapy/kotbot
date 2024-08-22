package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.Update

data class CommandExecutionContext(
    val command: Command,
    val update: Update,
    val message: Message,
)
