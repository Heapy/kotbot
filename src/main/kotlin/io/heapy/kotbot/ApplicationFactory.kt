package io.heapy.kotbot

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.configuration.Configuration
import io.heapy.kotbot.configuration.DefaultConfiguration
import io.heapy.kotbot.metrics.createPrometheusMeterRegistry
import io.heapy.kotbot.web.KtorServer
import io.heapy.kotbot.web.Server
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.micrometer.prometheus.PrometheusMeterRegistry

open class ApplicationFactory {
    open val configuration: Configuration by lazy {
        ConfigFactory.load().extract<DefaultConfiguration>()
    }

    open val prometheusMeterRegistry: PrometheusMeterRegistry by lazy {
        createPrometheusMeterRegistry(
            configuration = configuration.metrics
        )
    }

    open val httpClient: HttpClient by lazy {
        HttpClient(Apache) {
            install(JsonFeature) {
                serializer = JacksonSerializer()
            }
        }
    }

    open val deleteJoinRule: Rule by lazy(::DeleteJoinRule)

    open val deleteSpamRule: Rule by lazy(::DeleteSpamRule)

    open val deleteHelloRule: Rule by lazy(::DeleteHelloRule)

    open val longTimeNoSeeRule: Rule by lazy(::LongTimeNoSeeRule)

    open val deleteSwearingRule: Rule by lazy(::DeleteSwearingRule)

    open val deleteVoiceMessageRule: Rule by lazy(::DeleteVoiceMessageRule)

    open val deleteStickersRule: Rule by lazy(::DeleteStickersRule)

    open val combotCasRule: Rule by lazy {
        CombotCasRule(
            client = httpClient,
            casConfiguration = configuration.cas
        )
    }

    open val helloWorldCommand: Command by lazy(::HelloWorldCommand)

    open val chatInfoCommand: Command by lazy(::ChatInfoCommand)

    open val server: Server by lazy {
        KtorServer(
            metricsScrapper = prometheusMeterRegistry::scrape,
        )
    }

    open val groupInFamilyFilter: Filter by lazy {
        GroupInFamilyFilter(configuration.groups)
    }

    open val kotBot: KotBot by lazy {
        KotBot(
            configuration = configuration.bot,
            rules = listOf(
                deleteJoinRule,
                deleteSpamRule,
                deleteHelloRule,
                longTimeNoSeeRule,
                deleteSwearingRule,
                deleteVoiceMessageRule,
                deleteStickersRule,
                combotCasRule,
            ),
            commands = listOf(
                helloWorldCommand,
                chatInfoCommand,
            ),
            filters = listOf(
                groupInFamilyFilter
            ),
            meterRegistry = prometheusMeterRegistry
        )
    }

    open val kotbot: Kotbot by lazy {
        Kotbot(
            token = configuration.bot.token,
        )
    }

    open fun start() {
        server.start()

        LOGGER.info("Application started.")
    }

    companion object {
        private val LOGGER = logger<ApplicationFactory>()
    }
}
