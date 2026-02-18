package io.heapy.kotbot.bot.commands

import io.heapy.kotbot.bot.BanUserInReplyCallbackData
import io.heapy.kotbot.bot.DeleteMessageInReplyCallbackData
import io.heapy.kotbot.bot.DismissCallbackData
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.StrikeUserInReplyCallbackData
import io.heapy.kotbot.bot.banFrom
import io.heapy.kotbot.bot.delete
import io.heapy.kotbot.bot.execute
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.InlineKeyboardButton
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.bot.refLog
import io.heapy.kotbot.bot.use_case.callback.CallbackDataService
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.markdown.Markdown

class SpamCommand(
    private val kotbot: Kotbot,
    private val callbackDataService: CallbackDataService,
    private val notificationChannelId: Long,
    private val markdown: Markdown,
) : Command {
    override val name = "/spam"
    override val requiredContext = listOf(Command.Context.GROUP_CHAT)
    override val requiredAccess = Command.Access.USER

    context(
        _: TransactionContext,
        cex: CommandExecutionContext,
    )
    override suspend fun execute() {
        when (cex.currentAccess) {
            Command.Access.ADMIN,
            Command.Access.MODERATOR -> cex.executeModerator()

            Command.Access.USER -> cex.executeUser()
        }
    }

    private suspend fun CommandExecutionContext.executeModerator() {
        message.reply_to_message?.let { reply ->
            kotbot.executeSafely(reply.delete)
            kotbot.executeSafely(reply.banFrom)
        }
    }

    context(_: TransactionContext)
    private suspend fun CommandExecutionContext.executeUser() {
        val linkToMessage = if (message.chat.username != null) {
            "[Message](https://t.me/c/${message.chat.username}/${message.reply_to_message?.message_id})"
        } else {
            "Message \\(Chat: ${message.chat.title}\\)"
        }

        val message = """
            User ${message.from?.refLog} reported spam.

            $linkToMessage: ${message.reply_to_message?.text}
        """.trimIndent()

        kotbot.execute(
            SendMessage(
                chat_id = LongChatId(notificationChannelId),
                text = markdown.escape(message),
                parse_mode = ParseMode.MarkdownV2.name,
                reply_markup = InlineKeyboardMarkup(
                    inline_keyboard = listOf(
                        listOf(
                            InlineKeyboardButton(
                                text = "Ban",
                                callback_data = generateBanCallbackData(),
                            ),
                            InlineKeyboardButton(
                                text = "Offtopic",
                                callback_data = genereateOfftopicCallbackData(),
                            ),
                            InlineKeyboardButton(
                                text = "Delete",
                                callback_data = generateDeleteCallbackData(),
                            ),
                            InlineKeyboardButton(
                                text = "Dismiss",
                                callback_data = generateDismissCallbackData(),
                            ),
                        )
                    )
                )
            )
        )
    }

    context(_: TransactionContext)
    private suspend fun CommandExecutionContext.generateBanCallbackData(
    ): String {
        return callbackDataService
            .insert(
                BanUserInReplyCallbackData(
                    message = message,
                    reason = "Spam",
                )
            )
            ?: error("Failed to create callback data")
    }

    context(_: TransactionContext)
    private suspend fun CommandExecutionContext.genereateOfftopicCallbackData(
    ): String {
        return callbackDataService
            .insert(
                StrikeUserInReplyCallbackData(
                    message = message,
                    reason = "Offtopic",
                )
            )
            ?: error("Failed to create callback data")
    }

    context(_: TransactionContext)
    private suspend fun CommandExecutionContext.generateDeleteCallbackData(
    ): String {
        return callbackDataService
            .insert(
                DeleteMessageInReplyCallbackData(
                    message = message,
                    reason = "Offtopic",
                )
            )
            ?: error("Failed to create callback data")
    }

    context(_: TransactionContext)
    private suspend fun CommandExecutionContext.generateDismissCallbackData(
    ): String {
        return callbackDataService
            .insert(
                DismissCallbackData(
                    message = message,
                )
            )
            ?: error("Failed to create callback data")
    }
}
