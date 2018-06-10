package link.kotlin.kotbot

import link.kotlin.kotbot.configuration.DefaultKotbotConfiguration
import link.kotlin.kotbot.configuration.KotbotConfiguration
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.TelegramBotsApi
import org.telegram.telegrambots.exceptions.TelegramApiException

/**
 * Entry point of bot.
 *
 * @author Ruslan Ibragimov
 */
object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        val configuration: KotbotConfiguration = DefaultKotbotConfiguration()

        ApiContextInitializer.init()
        val botsApi = TelegramBotsApi()

        try {
            botsApi.registerBot(KotBot(configuration))
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }

        println("Hello, bot!")
    }
}
