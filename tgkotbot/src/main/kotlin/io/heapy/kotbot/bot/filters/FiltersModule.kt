package io.heapy.kotbot.bot.filters

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.KotlinChatBotConfigurationModule
import io.heapy.kotbot.bot.NotificationServiceModule
import io.heapy.kotbot.infra.debug.PrettyPrintModule

@Module
open class FiltersModule(
    private val kotlinChatBotConfigurationModule: KotlinChatBotConfigurationModule,
    private val notificationServiceModule: NotificationServiceModule,
    private val prettyPrintModule: PrettyPrintModule,
) {
    open val groupInFamilyFilter: Filter by lazy {
        KnownChatsFilter(
            config = kotlinChatBotConfigurationModule.groupsConfiguration,
            notificationService = notificationServiceModule.notificationService,
            prettyPrint = prettyPrintModule.prettyPrint,
        )
    }

    open val filter: Filter by lazy {
        Filter.combine(
            listOf(
                groupInFamilyFilter,
            )
        )
    }
}
