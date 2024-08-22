package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.commands.Command.Access.ADMIN
import io.heapy.kotbot.bot.commands.Command.Access.USER
import io.heapy.kotbot.bot.commands.Command.Context.GROUP_CHAT
import io.heapy.kotbot.bot.commands.Command.Context.USER_CHAT
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.infra.logger
import kotlinx.coroutines.coroutineScope

class CommandResolver(
    private val commands: List<Command>,
    private val commandExecutor: CommandExecutor,
    private val admins: List<Long>,
) {
    suspend fun findAndExecuteCommand(update: Update): Boolean = coroutineScope {
        val commandExecutionContext = update.toCommandExecutionContext()

        if (commandExecutionContext != null) {
            try {
                commandExecutor
                    .execute(
                        commandExecutionContext = commandExecutionContext,
                    )
            } catch (e: Exception) {
                log.error("Exception in command. Update: {}", update, e)
            }

            true
        } else {
            false
        }
    }

    internal fun Update.toCommandExecutionContext(): CommandExecutionContext? {
        val update = this
        return update.message?.let { message ->
            val command = commands.find { command ->
                command.name == update.name
                        && update.context in command.context
                        && command.access.isAllowed(update.access)
            }

            command?.let {
                CommandExecutionContext(
                    command = command,
                    message = message,
                    update = update,
                )
            }
        }
    }

    private val Update.context: Command.Context
        get() = when (message?.chat?.type) {
            "private" -> USER_CHAT
            else -> GROUP_CHAT
        }

    private val Update.access: Command.Access
        get() = message?.from?.id?.let { id ->
            if (admins.contains(id)) ADMIN else USER
        } ?: USER

    private val Update.name: String?
        get() = message?.text?.split(' ')?.getOrNull(0)

    private companion object {
        private val log = logger<CommandResolver>()
    }
}
