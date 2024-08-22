package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.*
import io.heapy.kotbot.bot.commands.Command.Access
import io.heapy.kotbot.bot.commands.Command.Context
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.DeleteMessage
import io.heapy.kotbot.bot.method.EditMessageText
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.ReplyParameters
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.infra.logger
import io.heapy.kotbot.infra.openai.GptService
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
        kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(update.cmdMessage.chat.id),
                text = """
                Chat id: ${update.cmdMessage.chat.id}
            """.trimIndent()
            )
        )
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
            kotbot.executeSafely(
                SendMessage(
                    chat_id = LongChatId(id),
                    text = text
                )
            )
            kotbot.executeSafely(
                SendMessage(
                    chat_id = LongChatId(admin),
                    text = """
                    ${update.cmdMessage.from?.username} sent following message to chat $name:
                    $text
                """.trimIndent()
                )
            )
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
            kotbot.executeSafely(
                SendMessage(
                    chat_id = LongChatId(update.cmdMessage.chat.id),
                    text = text,
                    parse_mode = "MarkdownV2",
                )
            )
        }
    }

    companion object {
        private val start_text = readResource("start.txt")
    }
}

class GptCommand(
    private val gptService: GptService,
) : Command {
    override val name: String = "/gpt"
    override val context: Context = Context.GROUP_CHAT
    override val access: Access = Access.ADMIN

    override suspend fun execute(
        kotbot: Kotbot,
        update: Update,
    ) {
        val text = update.message?.textWithoutCommand
        val replyText = update.message?.reply_to_message?.text
        val threadId = update.message?.message_thread_id

        kotbot.executeSafely(
            DeleteMessage(
                chat_id = LongChatId(update.cmdMessage.chat.id),
                message_id = update.cmdMessage.message_id,
            )
        )

        val replyParameters = update.message?.reply_to_message?.message_id?.let {
            ReplyParameters(
                message_id = it,
            )
        }

        val message = kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(update.cmdMessage.chat.id),
                message_thread_id = threadId,
                text = "Hey! You asked GPT to help, please wait...",
                reply_parameters = replyParameters,
                parse_mode = "MarkdownV2",
            )
        )

        val prompt = text
            ?: replyText
            ?: run {
                log.info("No text to process")
                return
            }

        val response = gptService
            .complete(
                prompt = prompt,
            )

        val escaped = response
            .replace(".", "\\.")
            .replace("!", "\\!")
            .replace("(", "\\(")
            .replace(")", "\\)")
            .replace("-", "\\-")
            .replace("@kotlin_lang", "@kotlin\\_lang")
            .replace("@kotlin_start", "@kotlin\\_start")

        log.info("GPT response: {}", response)
        log.info("Escaped response: {}", escaped)

        kotbot.executeSafely(
            EditMessageText(
                chat_id = LongChatId(update.cmdMessage.chat.id),
                message_id = message?.message_id,
                text = escaped,
                parse_mode = "MarkdownV2",
            )
        )
    }

    private companion object {
        private val log = logger<GptCommand>()
    }
}
