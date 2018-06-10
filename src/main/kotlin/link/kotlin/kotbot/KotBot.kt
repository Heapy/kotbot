package link.kotlin.kotbot

import link.kotlin.kotbot.configuration.KotbotConfiguration
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.bots.TelegramLongPollingBot

/**
 * @author Ruslan Ibragimov
 */
class KotBot(
    private val configuration: KotbotConfiguration
) : TelegramLongPollingBot() {
    override fun getBotToken() = configuration.token
    override fun getBotUsername() = configuration.name

    override fun onUpdateReceived(update: Update) {
        println(update.message.text)
    }
}
