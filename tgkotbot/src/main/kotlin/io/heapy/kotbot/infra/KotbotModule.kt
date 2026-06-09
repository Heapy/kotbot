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
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

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
                requestTimeoutMillis = 60_000
                socketTimeoutMillis = 60_000
            }
            // getUpdates long-polls for ~50s, so a keep-alive connection sits idle for
            // the whole poll. When the network/Telegram edge silently drops that pooled
            // connection, CIO reuses the dead socket on the next poll and the request
            // stalls until the 60s request timeout -- the bursts of
            // HttpRequestTimeoutException seen in the logs. Closing the connection after
            // each call forces a fresh socket per poll (cheap at ~1 request/min) and
            // removes stale-connection reuse from the polling path.
            defaultRequest {
                header(HttpHeaders.Connection, "close")
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
