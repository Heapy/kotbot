package io.heapy.kotbot

import io.heapy.komodo.logging.logger
import io.heapy.kotbot.bot.BotConfiguration
import io.heapy.kotbot.bot.KotlinTelegramLongPollingBot
import io.heapy.kotbot.bot.TelegramUpdate

class KotBot(
    configuration: BotConfiguration
) : KotlinTelegramLongPollingBot {
    override val token = configuration.token
    override val name = configuration.name

    override suspend fun handleUpdate(update: TelegramUpdate) {
        LOGGER.info(update.toString())
    }

    override fun onClosing() {
    }

    companion object {
        private val LOGGER = logger<KotBot>()
    }
}
