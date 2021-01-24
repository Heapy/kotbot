package io.heapy.kotbot.bot

import io.heapy.komodo.logging.logger
import io.heapy.komodo.shutdown.ShutdownManager
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

public interface TelegramBot {
    public fun start()
}

public class SimpleTelegramBot(
    private val configuration: BotConfiguration,
    private val shutdownManager: ShutdownManager,
    private val bot: KotlinBotSession
) : TelegramBot {
    override fun start() {
        try {
            bot.start()

            LOGGER.info("${configuration.name} started.")

            shutdownManager.addShutdownListener("Bot", 0) {
                bot.close()
            }
        } catch (e: TelegramApiException) {
            LOGGER.error("An error occurred in the bot", e)
            throw e
        }
    }

    private companion object {
        private val LOGGER = logger<SimpleTelegramBot>()
    }
}
