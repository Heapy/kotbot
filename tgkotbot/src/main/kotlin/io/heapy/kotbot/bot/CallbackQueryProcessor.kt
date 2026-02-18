package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.method.DeleteMessage
import io.heapy.kotbot.bot.method.EditMessageText
import io.heapy.kotbot.bot.model.CallbackQuery
import io.heapy.kotbot.bot.model.InaccessibleMessage
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.bot.use_case.callback.CallbackDataService
import io.heapy.kotbot.infra.jdbc.TransactionContext

class CallbackQueryProcessor(
    private val callbackDataService: CallbackDataService,
    private val kotbot: Kotbot,
    private val userContextService: UserContextService,
) {
    context(_: TransactionContext)
    suspend fun processCallbackQuery(
        callbackQuery: CallbackQuery,
    ) {
        val callbackData = callbackQuery.data
        callbackData?.let {
            val backData = callbackDataService.getById(callbackData)
            backData?.let {
                processCallbackData(
                    callbackQuery = callbackQuery,
                    callBackData = backData,
                )
            }
        }
    }

    context(_: TransactionContext)
    private suspend fun processCallbackData(
        callbackQuery: CallbackQuery,
        callBackData: KotbotCallBackData,
    ) {
        when (callBackData) {
            is BanUserInReplyCallbackData -> {
                when (val message = callbackQuery.message) {
                    is InaccessibleMessage, null -> error("Inaccessible message")
                    is Message -> {
                        callBackData.message.reply_to_message
                            ?.let {
                                kotbot.executeSafely(it.delete)
                                kotbot.executeSafely(it.banFrom)

                                kotbot.executeSafely(
                                    EditMessageText(
                                        chat_id = LongChatId(message.chat.id),
                                        message_id = message.message_id,
                                        reply_markup = null,
                                        text = buildString {
                                            append(message.text)
                                            appendLine()
                                            appendLine("Message deleted by ${callbackQuery.from.ref} and user ${it.from?.refLog} banned")
                                        }
                                    )
                                )
                            }
                    }
                }
            }

            is StrikeUserInReplyCallbackData -> {
                when (val message = callbackQuery.message) {
                    is InaccessibleMessage, null -> error("Inaccessible message")
                    is Message -> {
                        callBackData.message.reply_to_message
                            ?.let { strikeMessage ->
                                kotbot.executeSafely(strikeMessage.delete)
                                userContextService.addStrike(
                                    message = strikeMessage,
                                    reason = "Strike by moderator"
                                )

                                kotbot.executeSafely(
                                    EditMessageText(
                                        chat_id = LongChatId(message.chat.id),
                                        message_id = message.message_id,
                                        reply_markup = null,
                                        text = buildString {
                                            append(message.text)
                                            appendLine()
                                            appendLine("Message deleted by ${callbackQuery.from.ref} and user ${strikeMessage.from?.ref} received strike")
                                        }
                                    )
                                )
                            }
                    }
                }
            }

            is DeleteMessageInReplyCallbackData -> {
                when (val message = callbackQuery.message) {
                    is InaccessibleMessage, null -> error("Inaccessible message")
                    is Message -> {
                        callBackData.message.reply_to_message?.delete
                            ?. let {
                                kotbot.executeSafely(it)

                                kotbot.executeSafely(
                                    EditMessageText(
                                        chat_id = LongChatId(message.chat.id),
                                        message_id = message.message_id,
                                        reply_markup = null,
                                        text = buildString {
                                            append(message.text)
                                            appendLine()
                                            appendLine("Message deleted by ${callbackQuery.from.ref}")
                                        }
                                    )
                                )
                            }
                    }
                }
            }

            is DismissCallbackData -> {
                when (val message = callbackQuery.message) {
                    is InaccessibleMessage, null -> error("Inaccessible message")
                    is Message -> {
                        kotbot.executeSafely(
                            EditMessageText(
                                chat_id = LongChatId(message.chat.id),
                                message_id = message.message_id,
                                reply_markup = null,
                                text = buildString {
                                    append(message.text)
                                    appendLine()
                                    appendLine("Dismissed by ${callbackQuery.from.ref}")
                                }
                            )
                        )
                    }
                }
            }

            is SendGptMessageCallbackData -> {
                when (val message = callbackQuery.message) {
                    is InaccessibleMessage, null -> error("Inaccessible message")
                    is Message -> {
                        // Edit the "please wait" message in the group with the GPT response
                        kotbot.executeSafely(
                            EditMessageText(
                                chat_id = LongChatId(callBackData.groupChatId),
                                message_id = callBackData.waitMessageId,
                                text = callBackData.responseText,
                                parse_mode = ParseMode.MarkdownV2.name,
                            )
                        )

                        // Update the private chat message to indicate it was sent
                        kotbot.executeSafely(
                            EditMessageText(
                                chat_id = LongChatId(message.chat.id),
                                message_id = message.message_id,
                                reply_markup = null,
                                text = buildString {
                                    append(message.text)
                                    appendLine()
                                    appendLine("Sent to group")
                                }
                            )
                        )
                    }
                }
            }

            is DismissGptCallbackData -> {
                when (val message = callbackQuery.message) {
                    is InaccessibleMessage, null -> error("Inaccessible message")
                    is Message -> {
                        // Delete the "please wait" message from the group
                        kotbot.executeSafely(
                            DeleteMessage(
                                chat_id = LongChatId(callBackData.groupChatId),
                                message_id = callBackData.waitMessageId,
                            )
                        )

                        // Update the private chat message to indicate it was dismissed
                        kotbot.executeSafely(
                            EditMessageText(
                                chat_id = LongChatId(message.chat.id),
                                message_id = message.message_id,
                                reply_markup = null,
                                text = buildString {
                                    append(message.text)
                                    appendLine()
                                    appendLine("Dismissed")
                                }
                            )
                        )
                    }
                }
            }
        }
    }
}
