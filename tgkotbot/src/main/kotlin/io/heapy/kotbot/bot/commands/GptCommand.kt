package io.heapy.kotbot.bot.commands

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.escapeMarkdownV2
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.EditMessageText
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.ReplyParameters
import io.heapy.kotbot.bot.model.Update
import io.heapy.kotbot.infra.openai.GptService

class GptCommand(
    private val gptService: GptService,
) : Command {
    override val name: String = "/gpt"
    override val context = listOf(
        Command.Context.GROUP_CHAT,
        Command.Context.USER_CHAT,
    )
    override val access = Command.Access.ADMIN
    override val deleteCommandMessage = true

    override suspend fun execute(
        kotbot: Kotbot,
        update: Update,
        message: Message,
    ) {
        val text = message.textWithoutCommand
        val replyText = message.reply_to_message?.text
        val threadId = message.message_thread_id

        val replyParameters = message.reply_to_message?.message_id?.let {
            ReplyParameters(
                message_id = it,
            )
        }

        val prompt = text
            ?: replyText
            ?: run {
                log.info("No text to process")
                return
            }

        val sentMessage = kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(message.chat.id),
                message_thread_id = threadId,
                text = escapeMarkdownV2("Hey! You asked GPT to help, please wait..."),
                reply_parameters = replyParameters,
                parse_mode = "MarkdownV2",
            )
        )

        val response = gptService
            .complete(
                prompt = prompt,
            )

        val escaped = escapeMarkdownV2(response)

        log.info("GPT response: {}", response)
        log.info("Escaped response: {}", escaped)

        kotbot.executeSafely(
            EditMessageText(
                chat_id = LongChatId(message.chat.id),
                message_id = sentMessage?.message_id,
                text = escaped,
                parse_mode = "MarkdownV2",
            )
        )
    }

    private companion object : Logger()
}
