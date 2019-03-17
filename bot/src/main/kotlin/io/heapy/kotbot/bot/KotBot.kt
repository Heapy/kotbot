package io.heapy.kotbot.bot

import io.heapy.integration.slf4j.logger
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.groupadministration.KickChatMember
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
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

        if (update.message?.caption?.contains("t.me/joinchat/") == true) {
            LOGGER.info("Delete message with join link in caption ${update.message.text}")
            execute(DeleteMessage(update.message.chatId, update.message.messageId))
            execute(KickChatMember(update.message.chatId, update.message.from.id))
        }

        if (update.hasMessage()) {
            if (update.message.hasSticker()) {
                LOGGER.info("Delete message with sticker ${update.message.text}")
                execute(DeleteMessage(update.message.chatId, update.message.messageId))
            }

            if (update.message.hasText() && update.message.text.contains("t.me/joinchat/")) {
                LOGGER.info("Delete message with join link ${update.message.text}")
                execute(DeleteMessage(update.message.chatId, update.message.messageId))
                execute(KickChatMember(update.message.chatId, update.message.from.id))
            }
        }
    }

    companion object {
        private val LOGGER = logger<KotBot>()
    }
}
