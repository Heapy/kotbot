package io.heapy.tgpt.bot

import io.heapy.komok.tech.config.ConfigurationModule
import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.Kotbot
import io.heapy.tgpt.infra.configuration.BotConfiguration
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

@Module
open class KotbotModule(
    private val configurationModule: ConfigurationModule,
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
        }
    }

    open val kotbot: Kotbot by lazy {
        Kotbot(
            token = botConfiguration.token,
            httpClient = kotbotHttpClient,
        )
    }
}
