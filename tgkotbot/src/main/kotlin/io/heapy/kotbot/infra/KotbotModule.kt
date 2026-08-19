 package io.heapy.kotbot.infra

import io.heapy.komok.tech.config.ConfigurationModule
import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.use_case.history.LogUpdatesServiceModule
import io.heapy.kotbot.infra.configuration.BotConfiguration
import io.heapy.kotbot.infra.http_client.HttpRequestLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout

@Module
class KotbotModule(
    private val configurationModule: ConfigurationModule,
    private val logUpdatesServiceModule: LogUpdatesServiceModule,
) {
    val botConfiguration: BotConfiguration by lazy {
        configurationModule
            .config
            .read(
                deserializer = BotConfiguration.serializer(),
                path = "bot",
            )
    }

    val kotbotHttpClient by lazy {
        HttpClient(CIO) {
            install(HttpTimeout) {
                connectTimeoutMillis = 10_000
                // Telegram caps a long poll at ~50s whatever timeout the request asks for, so
                // this leaves a 10s margin. Some polls still answer past it -- that is expected
                // and handled by the retry in receiveUpdates, which logs it at WARN. Raising the
                // deadline to outrun those was measured and does not work: the late answers move
                // with it, and past DEFAULT_POLL_STALE_THRESHOLD a stuck poll trips
                // /healthcheck and gets the container restarted instead of simply retried.
                requestTimeoutMillis = 60_000
                socketTimeoutMillis = 60_000
            }
            install(HttpRequestLogger) {
                saveFunction = logUpdatesServiceModule
                    .logUpdatesService::save
            }
        }
    }

    val kotbot: Kotbot by lazy {
        Kotbot(
            token = botConfiguration.token,
            httpClient = kotbotHttpClient,
        )
    }
}
