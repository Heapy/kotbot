package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.infra.jdbc.TransactionContext

interface Command {
    val name: String
    val requiredContext: List<Context>
    val requiredAccess: Access

    context(
        _: TransactionContext,
        cex: CommandExecutionContext,
    )
    suspend fun execute()

    val Message.textWithoutCommand: String?
        get() = text
            ?.substringAfter(name)
            ?.trim()
            ?.takeIf(String::isNotBlank)

    enum class Context {
        USER_CHAT,
        GROUP_CHAT,
    }

    enum class Access {
        ADMIN,
        MODERATOR,
        USER;

        fun isAllowed(actual: Access): Boolean {
            return this >= actual
        }
    }
}
