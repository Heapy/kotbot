package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.delete
import io.heapy.kotbot.bot.executeSafely
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CommandExecutor(
    private val kotbot: Kotbot,
) {
    suspend fun execute(
        commandExecutionContext: CommandExecutionContext,
    ) = coroutineScope {
        val command = commandExecutionContext.command
        val update = commandExecutionContext.update
        val message = commandExecutionContext.message

        if (command.deleteCommandMessage) {
            launch {
                kotbot.executeSafely(message.delete)
            }
        }

        command.execute(kotbot, update, message)
    }
}
