package io.heapy.kotbot

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.heapy.kotbot.bot.KotBot
import io.heapy.kotbot.bot.command.HelloWorldCommand
import io.heapy.kotbot.bot.command.ChatInfoCommand
import io.heapy.kotbot.bot.rule.CombotCasRule
import io.heapy.kotbot.bot.rule.DeleteHelloRule
import io.heapy.kotbot.bot.rule.DeleteJoinRule
import io.heapy.kotbot.bot.rule.DeleteSpamRule
import io.heapy.kotbot.bot.rule.DeleteStickersRule
import io.heapy.kotbot.bot.rule.DeleteSwearingRule
import io.heapy.kotbot.bot.rule.DeleteVoiceMessageRule
import io.heapy.kotbot.bot.rule.LongTimeNoSeeRule
import io.heapy.kotbot.bot.startBot
import io.heapy.kotbot.configuration.DefaultConfiguration
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
        val configuration = ConfigFactory.load().extract<DefaultConfiguration>()
        val meterRegistry = createPrometheusMeterRegistry(configuration.metrics)
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer()
            }
        }
        val rules = listOf(
            DeleteJoinRule(),
            DeleteSpamRule(),
            DeleteHelloRule(),
            LongTimeNoSeeRule(),
            DeleteSwearingRule(),
            DeleteVoiceMessageRule(),
            DeleteStickersRule(),
            CombotCasRule(client, configuration.cas)
        )
        val commands = listOf(
            HelloWorldCommand(),
            ChatInfoCommand()
        )

        startServer(
            meterRegistry::scrape
        )

        val kotbot = {
            KotBot(
                configuration.bot,
                rules,
                commands,
                meterRegistry
            )
        }

        startBot(
            configuration.bot,
            kotbot
        )

        LOGGER.info("Application started.")
    }

    private val LOGGER = logger<Application>()
}
