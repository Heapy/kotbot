package io.heapy.kotbot.bot.rule

import org.telegram.telegrambots.meta.api.objects.Message

/**
 * @author Ruslan Ibragimov
 */
sealed class Action

data class DeleteMessageAction(
    val chatId: Long,
    val messageId: Int
) : Action()

data class KickUserAction(
    val chatId: Long,
    val userId: Int
) : Action()

@Suppress("FunctionName")
fun DeleteMessageAction(message: Message): DeleteMessageAction =
    DeleteMessageAction(message.chatId, message.messageId)
