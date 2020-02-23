package io.heapy.kotbot.bot

import io.heapy.logging.logger
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.generics.LongPollingBot

/**
 * Function which should start bot.
 *
 * @author Ruslan Ibragimov
 * @since 1.0.0
 */
fun startBot(
    configuration: BotConfiguration,
    kotbot: () -> LongPollingBot
): ShutdownBot {
    try {
        ApiContextInitializer.init()
        val bot = TelegramBotsApi()
            .registerBot(kotbot())
        LOGGER.info("${configuration.name} started.")
        return bot::stop
    } catch (e: TelegramApiException) {
        LOGGER.error("An error occurred in the bot", e)
        throw e
    }
}

private val LOGGER = logger<KotBot>()

typealias ShutdownBot = () -> Unit
