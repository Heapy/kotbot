package io.heapy.kotbot.infra

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.infra.configuration.BotConfiguration
import io.heapy.kotbot.infra.configuration.ConfigurationModule
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon

@Module
open class KotbotModule(
    private val configurationModule: ConfigurationModule,
) {
    @OptIn(ExperimentalSerializationApi::class)
    open val botConfiguration: BotConfiguration by lazy {
        Hocon.decodeFromConfig(
            BotConfiguration.serializer(),
            configurationModule.config.getConfig("bot"),
        )
    }

    open val kotbot: Kotbot by lazy {
        Kotbot(
            token = botConfiguration.token,
        )
    }
}
