package io.heapy.kotbot.bot.rule

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
