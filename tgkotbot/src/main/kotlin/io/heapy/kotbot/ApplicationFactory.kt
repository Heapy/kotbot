package io.heapy.kotbot

import com.typesafe.config.ConfigFactory
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.configuration.Configuration
import io.heapy.kotbot.metrics.createPrometheusMeterRegistry
import io.heapy.kotbot.web.KtorServer
import io.heapy.kotbot.web.Server
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.micrometer.prometheus.PrometheusMeterRegistry
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.json.Json

open class ApplicationFactory {
    @OptIn(ExperimentalSerializationApi::class)
    open val configuration: Configuration by lazy {
        Hocon.decodeFromConfig(Configuration.serializer(), ConfigFactory.load())
    }

    open val prometheusMeterRegistry: PrometheusMeterRegistry by lazy {
        createPrometheusMeterRegistry(
            configuration = configuration.metrics
        )
    }

    open val httpClient: HttpClient by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    open val deleteJoinRule: Rule by lazy(::DeleteJoinRule)

    open val deleteSpamRule: Rule by lazy(::DeleteSpamRule)

    open val deleteHelloRule: Rule by lazy(::DeleteHelloRule)

    open val longTimeNoSeeRule: Rule by lazy(::LongTimeNoSeeRule)

    open val kasperskyCareersRule: Rule by lazy(::KasperskyCareersRule)

    open val deleteSwearingRule: Rule by lazy(::DeleteSwearingRule)

    open val deleteVoiceMessageRule: Rule by lazy(::DeleteVoiceMessageRule)

    open val deleteVideoNoteMessageRule: Rule by lazy(::DeleteVideoNoteRule)

    open val deleteStickersRule: Rule by lazy(::DeleteStickersRule)

    open val combotCasRule: Rule by lazy {
        CombotCasRule(
            client = httpClient,
            casConfiguration = configuration.cas
        )
    }

    open val deletePropagandaRule: Rule by lazy(::DeletePropagandaRule)

    open val helloWorldCommand: Command by lazy(::HelloWorldCommand)

    open val chatInfoCommand: Command by lazy(::ChatInfoCommand)

    open val spamCommand: Command by lazy(::SpamCommand)

    open val startCommand: Command by lazy(::StartCommand)

    open val postToForumCommand: Command by lazy {
        PostToForumCommand(
            forum = configuration.groups.forum
        )
    }

    open val sendMessageToGroupCommands: List<Command> by lazy {
        configuration.groups.ids.map { (name, id) ->
            SendMessageFromBotCommand(
                admin = configuration.groups.admin,
                name = "/$name",
                id = id,
            )
        }
    }

    open val server: Server by lazy {
        KtorServer(
            metricsScrapper = prometheusMeterRegistry::scrape,
        )
    }

    open val userContextStore: UserContextStore by lazy {
        UserContextStore(
        )
    }

    open val groupInFamilyFilter: Filter by lazy {
        KnownChatsFilter(configuration.groups)
    }

    open val kotbot: Kotbot by lazy {
        Kotbot(
            token = configuration.bot.token,
        )
    }

    open val kotlinChatsBot: KotlinChatsBot by lazy {
        KotlinChatsBot(
            kotbot = kotbot,
            rules = listOf(
                deleteJoinRule,
                deleteSpamRule,
                deleteHelloRule,
                longTimeNoSeeRule,
                kasperskyCareersRule,
                deleteSwearingRule,
                deleteVoiceMessageRule,
                deleteVideoNoteMessageRule,
                deleteStickersRule,
                combotCasRule,
                deletePropagandaRule,
            ),
            commands = listOf(
                helloWorldCommand,
                chatInfoCommand,
                spamCommand,
                postToForumCommand,
                startCommand,
            ) + sendMessageToGroupCommands,
            filter = Filter.combine(
                listOf(
                    groupInFamilyFilter
                )
            ),
            meterRegistry = prometheusMeterRegistry,
            admins = configuration.groups.admins.flatMap { it.value }
        )
    }

    open suspend fun start() {
        server.start()
        kotlinChatsBot.start()

        LOGGER.info("Application started.")
    }

    companion object {
        private val LOGGER = logger<ApplicationFactory>()
    }
}
