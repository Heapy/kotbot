package io.heapy.kotbot.bot

import io.heapy.komodo.logging.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

public interface KotlinTelegramLongPollingBot {
    public val token: String
    public val name: String
    public suspend fun handleUpdate(update: TelegramUpdate)
    public fun onClosing()
}

internal class TelegramLongPollingBotKotlinWrapper(
    private val bot: KotlinTelegramLongPollingBot
) : TelegramLongPollingBot() {
    override fun getBotToken() = bot.token
    override fun getBotUsername() = bot.name

    private val supervisor = SupervisorJob()

    override fun onUpdateReceived(update: Update) {
        CoroutineScope(supervisor).launch {
            try {
                bot.handleUpdate(update.toTelegramUpdate().single())
            } catch (e: Exception) {
                LOGGER.error("Uncaught error {} processing update: {}", e.message, update)
            }
        }
    }

    private companion object {
        private val LOGGER = logger<TelegramLongPollingBotKotlinWrapper>()
    }
}

