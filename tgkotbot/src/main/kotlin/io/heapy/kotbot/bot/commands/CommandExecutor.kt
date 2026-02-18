package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.delete
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.infra.jdbc.TransactionContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CommandExecutor(
    private val kotbot: Kotbot,
) {
    context(_: TransactionContext)
    suspend fun execute(
        commandExecutionContext: CommandExecutionContext,
    ) = coroutineScope {
        val command = commandExecutionContext.command
        val message = commandExecutionContext.message
        val context = commandExecutionContext.currentContext

        // Keep commands only in user chats
        if (context != Command.Context.USER_CHAT) {
            launch(Dispatchers.Default) {
                kotbot.executeSafely(message.delete)
            }
        }

        context(commandExecutionContext) {
            command.execute()
        }
    }
}
