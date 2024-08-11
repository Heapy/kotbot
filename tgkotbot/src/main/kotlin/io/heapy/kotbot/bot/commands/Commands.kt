package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.banFrom
import io.heapy.kotbot.bot.commands.Command.Access
import io.heapy.kotbot.bot.commands.Command.Context
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.delete
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.infra.readResource

interface Command {
    val name: String
    val context: Context
    val access: Access

    suspend fun execute(
        kotbot: Kotbot,
        update: Update,
    )

    /**
     * Non-null access to message,
     * that should be present because previous checks.
     */
    val Update.cmdMessage: Message
        get() = message!!

    val Message.textWithoutCommand: String?
        get() = text?.substringAfter(name)?.takeIf { it.isNotBlank() }

    enum class Context {
        USER_CHAT,
        GROUP_CHAT
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

class ChatInfoCommand : Command {
    override val name = "/chat-info"
    override val context = Context.GROUP_CHAT
    override val access = Access.ADMIN

    override suspend fun execute(
        kotbot: Kotbot,
        update: Update,
    ) {
        kotbot.executeSafely(SendMessage(
            chat_id = LongChatId(update.cmdMessage.chat.id),
            text = """
                Chat id: ${update.cmdMessage.chat.id}
            """.trimIndent()
        ))
        kotbot.executeSafely(update.cmdMessage.delete)
    }
}

class SpamCommand : Command {
    override val name = "/spam"
    override val context = Context.GROUP_CHAT
    override val access = Access.ADMIN

    override suspend fun execute(
        kotbot: Kotbot,
        update: Update,
    ) {
        update.cmdMessage.reply_to_message?.let { reply ->
            kotbot.executeSafely(reply.delete)
            kotbot.executeSafely(reply.banFrom)
            kotbot.executeSafely(update.cmdMessage.delete)
        }
    }
}

class SendMessageFromBotCommand(
    private val admin: Long,
    override val name: String,
    val id: Long,
) : Command {
    override val context = Context.USER_CHAT
    override val access = Access.ADMIN

    override suspend fun execute(
        kotbot: Kotbot,
        update: Update,
    ) {
        update.cmdMessage.textWithoutCommand?.let { text ->
            kotbot.executeSafely(SendMessage(
                chat_id = LongChatId(id),
                text = text
            ))
            kotbot.executeSafely(SendMessage(
                chat_id = LongChatId(admin),
                text = """
                    ${update.cmdMessage.from?.username} sent following message to chat $name:
                    $text
                """.trimIndent()
            ))
        }
    }
}

class StartCommand : Command {
    override val name = "/start"
    override val context = Context.USER_CHAT
    override val access = Access.USER

    override suspend fun execute(
        kotbot: Kotbot,
        update: Update,
    ) {
        start_text?.let { text ->
            kotbot.executeSafely(SendMessage(
                chat_id = LongChatId(update.cmdMessage.chat.id),
                text = text,
                parse_mode = "MarkdownV2",
            ))
        }
    }

    companion object {
        private val start_text = readResource("start.txt")
    }
}
