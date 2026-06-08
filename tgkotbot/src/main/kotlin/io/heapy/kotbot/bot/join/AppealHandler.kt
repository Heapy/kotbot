package io.heapy.kotbot.bot.join

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.MessageHandler
import io.heapy.kotbot.bot.NotificationService
import io.heapy.kotbot.bot.executeSafely
import io.heapy.kotbot.bot.method.EditMessageText
import io.heapy.kotbot.bot.method.GetChat
import io.heapy.kotbot.bot.method.RestrictChatMember
import io.heapy.kotbot.bot.method.SendMessage
import io.heapy.kotbot.bot.model.CallbackQuery
import io.heapy.kotbot.bot.model.ChatPermissions
import io.heapy.kotbot.bot.model.LongChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.ParseMode
import io.heapy.kotbot.bot.ref
import io.heapy.kotbot.database.enums.JoinSessionStatus
import io.heapy.kotbot.database.enums.VerificationSource
import io.heapy.kotbot.infra.jdbc.TransactionContext
import io.heapy.kotbot.infra.markdown.Markdown

/**
 * Third stage of the join flow: the CAS appeal. As a [MessageHandler] it captures the free-text
 * appeal a read-only user sends in their private DM; it also resolves the admin Approve/Decline
 * decision that restores or keeps the restriction.
 */
class AppealHandler(
    private val kotbot: Kotbot,
    private val joinSessionDao: JoinSessionDao,
    private val verifiedUserDao: VerifiedUserDao,
    private val notificationService: NotificationService,
    private val markdown: Markdown,
    private val messages: JoinMessages,
) : MessageHandler {
    /**
     * Captures a free-text appeal sent as a private DM by a user who is [JoinSessionStatus.AWAITING_APPEAL].
     * Returns true if the message belonged to an appeal (so it is not routed to GPT/rules).
     */
    context(_: TransactionContext)
    override suspend fun handle(
        message: Message,
    ): Boolean {
        if (message.chat.type != "private") return false
        val from = message.from ?: return false
        val session = joinSessionDao.findAwaitingAppealByUser(
            telegramId = from.id,
            userChatId = message.chat.id,
        ) ?: return false

        val text = message.text?.trim()
        if (text.isNullOrBlank()) {
            // Sticker/photo/etc.: keep the session awaiting and ask for text, but still own the message.
            kotbot.executeSafely(
                SendMessage(
                    chat_id = LongChatId(message.chat.id),
                    text = markdown.escape("Please send a text explanation to appeal for write access."),
                    parse_mode = ParseMode.MarkdownV2.name,
                )
            )
            return true
        }

        joinSessionDao.submitAppeal(session.id, text)

        val keyboard = messages.buildAppealKeyboard(session.id)
        notificationService.notifyAdmins(messages.formatAppealForAdmins(session, from, text), keyboard)

        kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(message.chat.id),
                text = markdown.escape("✅ Your appeal has been submitted and is under review. You can read the group while you wait."),
                parse_mode = ParseMode.MarkdownV2.name,
            )
        )
        log.info("Appeal submitted by user {} for chat {} (session={})", from.id, session.chatId, session.id)
        return true
    }

    context(_: TransactionContext)
    suspend fun handleAppealDecision(
        callbackQuery: CallbackQuery,
        sessionId: Long,
        approve: Boolean,
    ) {
        val finalStatus = if (approve) JoinSessionStatus.PASSED else JoinSessionStatus.FAILED
        val session = joinSessionDao.decidePendingAppeal(sessionId, finalStatus)
        if (session == null) {
            // Stale/double click: a previous decision already resolved this appeal. Do not touch
            // Telegram moderation state; just reflect the current status on the admin message.
            val current = joinSessionDao.findById(sessionId)
            editAdminMessage(callbackQuery, "This appeal was already resolved (status: ${current?.status ?: "unknown"}).")
            return
        }

        val telegramId = session.telegramId
        if (approve) {
            kotbot.executeSafely(
                RestrictChatMember(
                    chat_id = LongChatId(session.chatId),
                    user_id = telegramId,
                    permissions = getDefaultChatPermissions(session.chatId),
                    use_independent_chat_permissions = true,
                )
            )
                ?: error("Failed to restore write access for approved appeal session $sessionId")

            verifiedUserDao.insertVerified(
                telegramId = telegramId,
                source = VerificationSource.MANUAL,
                sessionId = session.id,
            )
            editAdminMessage(callbackQuery, "✅ Appeal approved by ${callbackQuery.from.ref}.")
            dmUser(session.userChatId, "✅ Your appeal was approved — write access has been restored. Welcome!")
        } else {
            // Keep the read-only restriction in place.
            editAdminMessage(callbackQuery, "❌ Appeal declined by ${callbackQuery.from.ref}.")
            dmUser(session.userChatId, "❌ Your appeal was declined. You can still read the group.")
        }
        log.info("Appeal for session {} {} by admin {}", sessionId, if (approve) "approved" else "declined", callbackQuery.from.id)
    }

    private suspend fun editAdminMessage(
        callbackQuery: CallbackQuery,
        text: String,
    ) {
        val message = callbackQuery.message
        if (message is Message) {
            kotbot.executeSafely(
                EditMessageText(
                    chat_id = LongChatId(message.chat.id),
                    message_id = message.message_id,
                    text = markdown.escape(text),
                    parse_mode = ParseMode.MarkdownV2.name,
                    reply_markup = null,
                )
            )
        }
    }

    private suspend fun dmUser(
        userChatId: Long,
        text: String,
    ) {
        kotbot.executeSafely(
            SendMessage(
                chat_id = LongChatId(userChatId),
                text = markdown.escape(text),
                parse_mode = ParseMode.MarkdownV2.name,
            )
        )
    }

    private suspend fun getDefaultChatPermissions(
        chatId: Long,
    ): ChatPermissions {
        val chat = kotbot.executeSafely(
            GetChat(chat_id = LongChatId(chatId))
        ) ?: error("Failed to get chat defaults for chat $chatId")

        return chat.permissions ?: error("Chat $chatId did not return default member permissions")
    }

    private companion object : Logger()
}
