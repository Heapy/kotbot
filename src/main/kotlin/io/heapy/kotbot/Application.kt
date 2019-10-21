package io.heapy.kotbot

import io.heapy.kotbot.bot.rule.*
import io.heapy.kotbot.bot.startBot
import io.heapy.kotbot.configuration.Configuration
import io.heapy.kotbot.metrics.createPrometheusMeterRegistry
import io.heapy.kotbot.web.startServer
import io.heapy.logging.logger

/**
 * Entry point of bot.
 *
 * @author Ruslan Ibragimov
 */
object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        val configuration = Configuration()
        val metricsRegistry = createPrometheusMeterRegistry(configuration)
        val rules = listOf(
            DeleteJoinRule(),
            DeleteSpamRule(),
            DeleteHelloRule(),
            DeleteSwearingRule(),
            DeleteVoiceMessageRule()
        )

        startServer(
            metricsRegistry
        )

        startBot(
            configuration,
            rules
        )

        LOGGER.info("Application started.")
    }

    private val LOGGER = logger<Application>()
}
