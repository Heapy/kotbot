package io.heapy.kotbot

import org.telegram.telegrambots.meta.api.objects.Message

/**
 * @author Ruslan Ibragimov
 */
public sealed interface Action

public data class DeleteMessageAction(
    val chatId: Long,
    val messageId: Int,
) : Action

public data class BanMemberAction(
    val chatId: Long,
    val userId: Long,
) : Action

public data class ReplyAction(
    val chatId: Long,
    val message: String,
) : Action

@Suppress("FunctionName")
public fun DeleteMessageAction(message: Message): DeleteMessageAction =
    DeleteMessageAction(message.chatId, message.messageId)
