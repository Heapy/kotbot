package io.heapy.kotbot.bot.action

import org.telegram.telegrambots.meta.api.objects.Message

/**
 * @author Ruslan Ibragimov
 */
sealed class Action

data class DeleteMessageAction(
    val chatId: Long,
    val messageId: Int,
) : Action()

data class BanMemberAction(
    val chatId: Long,
    val userId: Long,
) : Action()

data class ReplyAction(
    val chatId: Long,
    val message: String,
) : Action()

@Suppress("FunctionName")
fun DeleteMessageAction(message: Message): DeleteMessageAction =
    DeleteMessageAction(message.chatId, message.messageId)
