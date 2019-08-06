package io.heapy.kotbot.bot.rule

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

/**
 * @author Ruslan Ibragimov
 */
sealed class Action

sealed class ChatAction : Action() {
    abstract val chatId: Long
}

data class DeleteMessageAction(
    override val chatId: Long,
    val messageId: Int
) : ChatAction()

data class KickUserAction(
    override val chatId: Long,
    val userId: Int
) : ChatAction()

data class SendMessageAction(
    override val chatId: Long,
    val text: String,
    val inlineKeyboard: List<List<InlineKeyboardButton>>? = null
): ChatAction()
