package io.heapy.kotbot.bot.rule

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

/**
 * Represents an action, which can be invoked by the [Rule] during processing an update.
 * @author Ruslan Ibragimov
 */
sealed class Action

/**
 * Represents an [Action] connected to a chat. Can be retried if previous invocation failed.
 */
sealed class ChatAction : Action() {
    abstract val chatId: Long
}

/**
 * An [Action] which deletes message [messageId] from chat [chatId].
 */
data class DeleteMessageAction(
    override val chatId: Long,
    val messageId: Int
) : ChatAction()

/**
 * An [Action] which kicks user [userId] from chat [chatId].
 */
data class KickUserAction(
    override val chatId: Long,
    val userId: Int
) : ChatAction()

/**
 * An [Action] which sends message [text], optionally having buttons as an [inlineKeyboard], to chat [chatId].
 * TODO: abstract away inline keyboard.
 */
data class SendMessageAction(
    override val chatId: Long,
    val text: String,
    val inlineKeyboard: List<List<InlineKeyboardButton>>? = null
): ChatAction()
