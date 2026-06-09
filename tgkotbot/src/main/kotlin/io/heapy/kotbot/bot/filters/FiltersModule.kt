package io.heapy.kotbot.bot.filters

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.KotlinChatBotConfigurationModule
import io.heapy.kotbot.bot.NotificationServiceModule
import io.heapy.kotbot.infra.KotbotModule
import io.heapy.kotbot.infra.debug.PrettyPrintModule

@Module
class FiltersModule(
    private val kotlinChatBotConfigurationModule: KotlinChatBotConfigurationModule,
    private val notificationServiceModule: NotificationServiceModule,
    private val prettyPrintModule: PrettyPrintModule,
    private val kotbotModule: KotbotModule,
) {
    val groupInFamilyFilter: Filter by lazy {
        KnownChatsFilter(
            kotbot = kotbotModule.kotbot,
            wellKnownChats = kotlinChatBotConfigurationModule.wellKnownChats,
            notificationService = notificationServiceModule.notificationService,
            prettyPrint = prettyPrintModule.prettyPrint,
        )
    }

    val filter: Filter by lazy {
        Filter.combine(
            listOf(
                groupInFamilyFilter,
            )
        )
    }
}
