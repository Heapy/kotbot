package io.heapy.kotbot.bot

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.configuration.ConfigurationModule
import io.heapy.kotbot.infra.configuration.KnownChatsConfiguration
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon

@Module
open class KotlinChatBotConfigurationModule(
    private val configurationModule: ConfigurationModule,
) {
    @OptIn(ExperimentalSerializationApi::class)
    open val groupsConfiguration: KnownChatsConfiguration by lazy {
        Hocon.decodeFromConfig(
            KnownChatsConfiguration.serializer(),
            configurationModule.config.getConfig("groups"),
        )
    }
}
