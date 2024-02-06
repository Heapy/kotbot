package io.heapy.kotbot

import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.configuration.Configuration
import io.heapy.kotbot.dao.UpdateDao
import io.heapy.kotbot.dao.UserContextDao
import io.heapy.kotbot.metrics.createPrometheusMeterRegistry
import io.heapy.kotbot.web.KtorServer
import io.heapy.kotbot.web.Server
import io.heapy.kotbot.web.routes.DatabaseHealthCheck
import io.heapy.kotbot.web.routes.CombinedHealthCheck
import io.heapy.kotbot.web.routes.PingHealthCheck
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.micrometer.core.instrument.binder.db.MetricsDSLContext
import io.micrometer.prometheus.PrometheusMeterRegistry
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.json.Json
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.postgresql.ds.PGSimpleDataSource
import runMigrations
import java.lang.management.ManagementFactory
import kotlin.concurrent.thread

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

    open val updateDao by lazy {
        UpdateDao()
    }

    open val userContextDao by lazy {
        UserContextDao()
    }

    open val dslContext: DSLContext by lazy {
        System.setProperty("org.jooq.no-logo", "true")
        System.setProperty("org.jooq.no-tips", "true")
        MetricsDSLContext.withMetrics(
            DSL.using(hikariDataSource, SQLDialect.POSTGRES),
            prometheusMeterRegistry,
            emptyList(),
        )
    }

    open val hikariConfig: HikariConfig by lazy {
        HikariConfig().apply {
            dataSourceClassName = PGSimpleDataSource::class.qualifiedName
            username = configuration.jdbc.user
            password = configuration.jdbc.password
            dataSourceProperties["databaseName"] = configuration.jdbc.database
            dataSourceProperties["serverName"] = configuration.jdbc.host
            dataSourceProperties["portNumber"] = configuration.jdbc.port
        }
    }

    open val hikariDataSource: HikariDataSource by lazy {
        HikariDataSource(hikariConfig)
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
            healthCheck = healthCheck,
        )
    }

    open val healthCheck by lazy {
        CombinedHealthCheck(
            healthChecks = listOf(
                PingHealthCheck(),
                DatabaseHealthCheck(
                    dataSource = hikariDataSource,
                ),
            )
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

    open val applicationScope by lazy {
        CoroutineScope(Dispatchers.Default)
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
            admins = configuration.groups.admins.flatMap { it.value },
            updateDao = updateDao,
            dslContext = dslContext,
            applicationScope = applicationScope,
        )
    }

    open val main by lazy {
        CompletableDeferred<Unit>()
    }

    open suspend fun start() {
        runMigrations(hikariDataSource)
        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            log.info("Shutdown hook called.")
            applicationScope.cancel("Shutdown hook called.")
            main.complete(Unit)
        })

        server.start()
        kotlinChatsBot.start()

        log.info("Application started in ${uptime}ms.")

        main.await()
        log.info("Main gracefully stopped.")
    }

    companion object {
        private val log = logger<ApplicationFactory>()
    }
}

private inline val uptime: Long
    get() = ManagementFactory.getRuntimeMXBean().uptime
