package io.heapy.kotbot.bot

import io.heapy.komok.tech.config.ConfigurationModule
import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.configuration.KnownChatsConfiguration

@Module
class KotlinChatBotConfigurationModule(
    private val configurationModule: ConfigurationModule,
) {
    val groupsConfiguration: KnownChatsConfiguration by lazy {
        configurationModule
            .config
            .read(
                deserializer = KnownChatsConfiguration.serializer(),
                path = "groups",
            )
    }

    val wellKnownChats by lazy {
        groupsConfiguration.allowedGroups.values.toSet() + groupsConfiguration.notificationChannel
    }
}
