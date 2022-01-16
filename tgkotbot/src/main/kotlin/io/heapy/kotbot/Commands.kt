package io.heapy.kotbot

import io.heapy.kotbot.Command.Access
import io.heapy.kotbot.Command.Context
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.SendMessage
import io.heapy.kotbot.bot.Update
import io.heapy.kotbot.bot.Message

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
        USER;

        fun isAllowed(actual: Access): Boolean {
            return this >= actual
        }
    }
}

class HelloWorldCommand : Command {
    override val name = "/hello"
    override val context = Context.USER_CHAT
    override val access = Access.ADMIN

    override suspend fun execute(
        kotbot: Kotbot,
        update: Update,
    ) {
        kotbot.executeSafely(SendMessage(
            chat_id = update.cmdMessage.chat.id.toString(),
            text = update.cmdMessage.textWithoutCommand ?: "Hello, world!"
        ))
        kotbot.executeSafely(update.cmdMessage.delete)
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
            chat_id = update.cmdMessage.chat.id.toString(),
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

class PostToForumCommand(
    private val forum: Long,
) : Command {
    override val name = "/post"
    override val context = Context.USER_CHAT
    override val access = Access.USER

    override suspend fun execute(
        kotbot: Kotbot,
        update: Update,
    ) {
        update.cmdMessage.textWithoutCommand?.let { message ->
            val forumMessage = kotbot.executeSafely(SendMessage(
                chat_id = forum.toString(),
                text = message
            ))
            kotbot.executeSafely(SendMessage(
                chat_id = update.cmdMessage.chat.id.toString(),
                text = "Message posted to forum: https://t.me/kotlin_forum/${forumMessage?.message_id}"
            ))
            return
        }

        post_text?.let { text ->
            kotbot.executeSafely(SendMessage(
                chat_id = update.cmdMessage.chat.id.toString(),
                text = text,
                parse_mode = "MarkdownV2",
            ))

        }
    }

    companion object {
        private val post_text = readResource("post.txt")
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
                chat_id = id.toString(),
                text = text
            ))
            kotbot.executeSafely(SendMessage(
                chat_id = admin.toString(),
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
                chat_id = update.cmdMessage.chat.id.toString(),
                text = text,
                parse_mode = "MarkdownV2",
            ))
        }
    }

    companion object {
        private val start_text = readResource("start.txt")
    }
}
