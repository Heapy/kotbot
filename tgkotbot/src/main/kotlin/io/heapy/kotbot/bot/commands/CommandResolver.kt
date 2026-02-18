package io.heapy.kotbot.bot.commands

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.UserContextService
import io.heapy.kotbot.bot.commands.Command.Access.USER
import io.heapy.kotbot.bot.commands.Command.Context.GROUP_CHAT
import io.heapy.kotbot.bot.commands.Command.Context.USER_CHAT
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.infra.jdbc.TransactionContext
import kotlinx.coroutines.coroutineScope

class CommandResolver(
    private val commands: List<Command>,
    private val commandExecutor: CommandExecutor,
    private val userContextService: UserContextService,
) {
    context(_: TransactionContext)
    suspend fun findAndExecuteCommand(
        message: Message,
    ): Boolean = coroutineScope {
        val commandExecutionContext = message.toCommandExecutionContext()

        if (commandExecutionContext != null) {
            try {
                commandExecutor
                    .execute(
                        commandExecutionContext = commandExecutionContext,
                    )
            } catch (e: Exception) {
                log.error("Exception in command. Message: {}", message, e)
            }

            true
        } else {
            false
        }
    }

    context(_: TransactionContext)
    internal suspend fun Message.toCommandExecutionContext(
    ): CommandExecutionContext? {
        val message = this
        val context = message.getContext()
        val access = message.getAccess()

        val command = commands.find { command ->
            command.name == message.getName()
                    && context in command.requiredContext
                    && command.requiredAccess.isAllowed(access)
        }

        return command?.let {
            CommandExecutionContext(
                command = command,
                message = message,
                currentContext = context,
                currentAccess = access,
            )
        }
    }

    private fun Message.getContext(): Command.Context =
        when (chat.type) {
            "private" -> USER_CHAT
            else -> GROUP_CHAT
        }

    context(_: TransactionContext)
    private suspend fun Message.getAccess(): Command.Access {
        return from
            ?.id
            ?.let { id ->
                userContextService
                    .userAccess(
                        id = id,
                    )
            }
            ?: USER
    }

    private fun Message.getName(): String? =
        text?.substringBefore(' ')

    private companion object : Logger()
}
