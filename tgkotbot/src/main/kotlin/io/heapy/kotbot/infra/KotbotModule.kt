 package io.heapy.kotbot.infra

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.infra.configuration.BotConfiguration
import io.heapy.komok.tech.config.ConfigurationModule
import io.heapy.kotbot.bot.use_case.history.LogUpdatesServiceModule
import io.heapy.kotbot.infra.http_client.HttpRequestLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

@Module
open class KotbotModule(
    private val configurationModule: ConfigurationModule,
    private val logUpdatesServiceModule: LogUpdatesServiceModule,
) {
    open val botConfiguration: BotConfiguration by lazy {
        configurationModule
            .config
            .read(
                deserializer = BotConfiguration.serializer(),
                path = "bot",
            )
    }

    open val kotbotHttpClient by lazy {
        HttpClient(CIO) {
            engine {
                requestTimeout = 60_000
            }
            install(HttpRequestLogger) {
                saveFunction = logUpdatesServiceModule
                    .logUpdatesService::save
            }
        }
    }

    open val kotbot: Kotbot by lazy {
        Kotbot(
            token = botConfiguration.token,
            httpClient = kotbotHttpClient,
        )
    }
}
