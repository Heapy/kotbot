package io.heapy.kotbot.bot

import io.heapy.integration.slf4j.logger
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.groupadministration.KickChatMember
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * @author Ruslan Ibragimov
 */
class KotBot(
    private val configuration: BotConfiguration
) : TelegramLongPollingBot() {
    override fun getBotToken() = configuration.token
    override fun getBotUsername() = configuration.name

    override fun onUpdateReceived(update: Update) {
        LOGGER.debug(update.toString())

        if (update.message?.newChatMembers != null) {
            LOGGER.info("Delete message ${update.message.text}")
            LOGGER.info("Joined users ${update.message.newChatMembers}")
            execute(DeleteMessage(update.message.chatId, update.message.messageId))
        }

        if (update.anyMessage?.hasSticker() == true) {
            LOGGER.info("Delete message with sticker ${update.message.text}")
            execute(DeleteMessage(update.message.chatId, update.message.messageId))
        }

        update.anyText?.also { text ->
            when {
                text.contains("t.me/joinchat/") -> {
                    LOGGER.info("Delete message with join link ${update.message.text}")
                    execute(DeleteMessage(update.message.chatId, update.message.messageId))
                    execute(KickChatMember(update.message.chatId, update.message.from.id))
                }
                text.contains("t.cn/") -> {
                    LOGGER.info("Delete message with t.cn link ${update.message.text}")
                    execute(DeleteMessage(update.message.chatId, update.message.messageId))
                    execute(KickChatMember(update.message.chatId, update.message.from.id))
                }
            }
        }
    }

    private val Update.anyMessage: Message?
        get() = editedMessage ?: message

    private val Update.anyText: String?
        get() = anyMessage?.let { it.caption ?: it.text }

    companion object {
        private val LOGGER = logger<KotBot>()
    }
}
