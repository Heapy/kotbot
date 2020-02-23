package io.heapy.kotbot

import io.heapy.kotbot.bot.KotBot
import io.heapy.kotbot.bot.rule.CombotCasRule
import io.heapy.kotbot.bot.rule.DeleteHelloRule
import io.heapy.kotbot.bot.rule.DeleteJoinRule
import io.heapy.kotbot.bot.rule.DeleteSpamRule
import io.heapy.kotbot.bot.rule.DeleteSwearingRule
import io.heapy.kotbot.bot.rule.DeleteVoiceMessageRule
import io.heapy.kotbot.bot.startBot
import io.heapy.kotbot.configuration.Configuration
import io.heapy.kotbot.metrics.createPrometheusMeterRegistry
import io.heapy.kotbot.web.startServer
import io.heapy.logging.logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature

/**
 * Entry point of bot.
 *
 * @author Ruslan Ibragimov
 */
object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        val configuration = Configuration()
        val meterRegistry = createPrometheusMeterRegistry(configuration)
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer()
            }
        }
        val rules = listOf(
            DeleteJoinRule(),
            DeleteSpamRule(),
            DeleteHelloRule(),
            DeleteSwearingRule(),
            DeleteVoiceMessageRule(),
            CombotCasRule(client)
        )

        startServer(
            meterRegistry::scrape
        )

        val kotbot = { KotBot(configuration, rules, meterRegistry) }

        startBot(
            configuration,
            kotbot
        )

        LOGGER.info("Application started.")
    }

    private val LOGGER = logger<Application>()
}
