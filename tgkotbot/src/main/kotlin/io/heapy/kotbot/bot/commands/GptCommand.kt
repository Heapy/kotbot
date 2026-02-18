package io.heapy.kotbot.bot.commands

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.DismissGptCallbackData
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.SendGptMessageCallbackData
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.EditMessageText
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.InlineKeyboardButton
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.bot.model.ReplyParameters
import io.heapy.kotbot.bot.use_case.callback.CallbackDataService
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.markdown.Markdown
import io.heapy.kotbot.infra.openai.GptService

class GptCommand(
    private val kotbot: Kotbot,
    private val gptService: GptService,
    private val markdown: Markdown,
    private val callbackDataService: CallbackDataService,
) : Command {
    override val name: String = "/gpt"
    override val requiredContext = listOf(
        Command.Context.GROUP_CHAT,
        Command.Context.USER_CHAT,
    )
    override val requiredAccess = Command.Access.MODERATOR

    context(
        _: TransactionContext,
        cex: CommandExecutionContext,
    )
    override suspend fun execute() {
        when (cex.currentContext) {
            Command.Context.GROUP_CHAT -> executeInGroup()
            Command.Context.USER_CHAT -> executeInUserChat()
        }
    }

    context(
        _: TransactionContext,
        cex: CommandExecutionContext,
    )
    private suspend fun executeInGroup() {
        val message = cex.message
        val text = message.textWithoutCommand
        val replyText = message.reply_to_message?.text
        // Somehow message can have thread_id, even if it's not a forum
        val threadId = message.message_thread_id?.takeIf { message.chat.is_forum == true }

        val prompt = text
            ?: replyText
            ?: run {
                log.info("No text to process")
                return
            }

        // Send "please wait" in the group, replying to the original message
        val waitMessage = kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(message.chat.id),
                text = markdown.escape("Processing your request, please wait..."),
                reply_parameters = ReplyParameters(message_id = message.message_id),
                parse_mode = ParseMode.MarkdownV2.name,
                message_thread_id = threadId,
            )
        )

        if (waitMessage == null) {
            log.error("Failed to send wait message in group")
            return
        }

        val response = gptService.complete(userPrompt = prompt)
        val escaped = markdown.escape(response)

        log.info("GPT response: {}", response)
        log.info("Escaped response: {}", escaped)

        // Send preview to user's private chat with Send/Dismiss buttons
        kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(message.from!!.id),
                text = escaped,
                parse_mode = ParseMode.MarkdownV2.name,
                reply_markup = buildGptReplyMarkup(
                    groupChatId = message.chat.id,
                    waitMessageId = waitMessage.message_id,
                    responseText = escaped,
                ),
            )
        )
    }

    context(
        _: TransactionContext,
        cex: CommandExecutionContext,
    )
    private suspend fun executeInUserChat() {
        val message = cex.message
        val text = message.textWithoutCommand
        val replyText = message.reply_to_message?.text

        val prompt = text
            ?: replyText
            ?: run {
                log.info("No text to process")
                return
            }

        // Check if this is a reply to a GPT preview (refinement flow)
        val groupContext = extractGroupContext(message)

        if (groupContext != null) {
            // Show processing state by editing the preview message
            kotbot.executeSafely(
                EditMessageText(
                    chat_id = LongChatId(message.chat.id),
                    message_id = message.reply_to_message!!.message_id,
                    text = markdown.escape("Processing your refinement, please wait..."),
                    parse_mode = ParseMode.MarkdownV2.name,
                )
            )

            val response = gptService.complete(userPrompt = prompt)
            val escaped = markdown.escape(response)

            log.info("GPT refinement response: {}", response)

            // Edit the preview message with new response and buttons
            kotbot.executeSafely(
                EditMessageText(
                    chat_id = LongChatId(message.chat.id),
                    message_id = message.reply_to_message!!.message_id,
                    text = escaped,
                    parse_mode = ParseMode.MarkdownV2.name,
                    reply_markup = buildGptReplyMarkup(
                        groupChatId = groupContext.groupChatId,
                        waitMessageId = groupContext.waitMessageId,
                        responseText = escaped,
                    ),
                )
            )
        } else {
            // Direct GPT response (no group context)
            val sentMessage = kotbot.executeSafely(
                SendMessage(
                    chat_id = LongChatId(message.from!!.id),
                    text = markdown.escape("Processing your request, please wait..."),
                    parse_mode = ParseMode.MarkdownV2.name,
                )
            )

            val response = gptService.complete(userPrompt = prompt)
            val escaped = markdown.escape(response)

            kotbot.executeSafely(
                EditMessageText(
                    chat_id = LongChatId(message.from!!.id),
                    message_id = sentMessage?.message_id,
                    text = escaped,
                    parse_mode = ParseMode.MarkdownV2.name,
                )
            )
        }
    }

    /**
     * Extract group context from the reply_to_message's inline keyboard buttons.
     * Consumes the old Send callback data since it will be replaced with new buttons.
     */
    context(_: TransactionContext)
    private suspend fun extractGroupContext(message: Message): GroupContext? {
        val replyMarkup = message.reply_to_message?.reply_markup ?: return null
        val sendButton = replyMarkup.inline_keyboard
            .flatten()
            .find { it.text == "Send" }
        val callbackDataId = sendButton?.callback_data ?: return null

        val callbackData = callbackDataService.getById(callbackDataId)
            as? SendGptMessageCallbackData ?: return null

        return GroupContext(
            groupChatId = callbackData.groupChatId,
            waitMessageId = callbackData.waitMessageId,
        )
    }

    context(_: TransactionContext)
    private suspend fun buildGptReplyMarkup(
        groupChatId: Long,
        waitMessageId: Int,
        responseText: String,
    ): InlineKeyboardMarkup {
        val sendCallbackData = callbackDataService.insert(
            SendGptMessageCallbackData(
                groupChatId = groupChatId,
                waitMessageId = waitMessageId,
                responseText = responseText,
            )
        ) ?: error("Failed to create send callback data")

        val dismissCallbackData = callbackDataService.insert(
            DismissGptCallbackData(
                groupChatId = groupChatId,
                waitMessageId = waitMessageId,
            )
        ) ?: error("Failed to create dismiss callback data")

        return InlineKeyboardMarkup(
            inline_keyboard = listOf(
                listOf(
                    InlineKeyboardButton(
                        text = "Send",
                        callback_data = sendCallbackData,
                    ),
                    InlineKeyboardButton(
                        text = "Dismiss",
                        callback_data = dismissCallbackData,
                    ),
                )
            )
        )
    }

    private data class GroupContext(
        val groupChatId: Long,
        val waitMessageId: Int,
    )

    private companion object : Logger()
}