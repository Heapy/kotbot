package io.heapy.kotbot.infra

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.infra.configuration.BotConfiguration
import io.heapy.komok.tech.config.ConfigurationModule

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

    open val kotbot: Kotbot by lazy {
        Kotbot(
            token = botConfiguration.token,
        )
    }
}
