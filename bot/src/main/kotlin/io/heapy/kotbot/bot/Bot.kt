package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.rule.Rule
import io.heapy.logging.logger
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

/**
 * Function which should start bot.
 *
 * @author Ruslan Ibragimov
 * @since 1.0.0
 */
fun startBot(
    configuration: BotConfiguration,
    rules: List<Rule>,
    state: State
): ShutdownBot {
    try {
        ApiContextInitializer.init()
        val bot = TelegramBotsApi()
            .registerBot(KotBot(configuration, rules, state))
        LOGGER.info("${configuration.name} started.")
        return bot::stop
    } catch (e: TelegramApiException) {
        LOGGER.error("An error occurred in the bot", e)
        throw e
    }
}

private val LOGGER = logger<KotBot>()

typealias ShutdownBot = () -> Unit
