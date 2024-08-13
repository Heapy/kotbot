package io.heapy.kotbot.bot

import io.heapy.komok.tech.config.ConfigurationModule
import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.configuration.KnownChatsConfiguration

@Module
open class KotlinChatBotConfigurationModule(
    private val configurationModule: ConfigurationModule,
) {
    open val groupsConfiguration: KnownChatsConfiguration by lazy {
        configurationModule
            .config
            .read(
                deserializer = KnownChatsConfiguration.serializer(),
                path = "groups",
            )
    }
}
