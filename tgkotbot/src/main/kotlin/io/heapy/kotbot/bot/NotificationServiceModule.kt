package io.heapy.kotbot.bot

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.KotbotModule

@Module
open class NotificationServiceModule(
    private val kotbotModule: KotbotModule,
    private val kotlinChatBotConfigurationModule: KotlinChatBotConfigurationModule,
) {
    open val notificationService: NotificationService by lazy {
        NotificationService(
            kotbot = kotbotModule.kotbot,
            chatId = kotlinChatBotConfigurationModule.groupsConfiguration.notificationChannel,
        )
    }
}
