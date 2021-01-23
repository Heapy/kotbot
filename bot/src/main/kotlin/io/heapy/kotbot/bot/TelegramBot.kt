package io.heapy.kotbot.bot

import io.heapy.komodo.logging.logger
import io.heapy.komodo.shutdown.ShutdownManager
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

interface TelegramBot {
    fun start()
}

class KotbotTelegramBot(
    private val configuration: BotConfiguration,
    private val shutdownManager: ShutdownManager,
    private val kotBot: KotBot
) : TelegramBot {
    override fun start() {
        try {
            val bot = TelegramBotsApi(DefaultBotSession::class.java)
            val session = bot.registerBot(kotBot)
            LOGGER.info("${configuration.name} started.")

            shutdownManager.addShutdownListener("Bot", 0) {
                session.stop()
            }
        } catch (e: TelegramApiException) {
            LOGGER.error("An error occurred in the bot", e)
            throw e
        }
    }

    companion object {
        private val LOGGER = logger<KotbotTelegramBot>()
    }
}
