package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.Update

interface Command {
    val name: String
    val context: List<Context>
    val access: Access
    val deleteCommandMessage: Boolean

    suspend fun execute(
        kotbot: Kotbot,
        update: Update,
        message: Message,
    )

    val Message.textWithoutCommand: String?
        get() = text
            ?.substringAfter(name)
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
