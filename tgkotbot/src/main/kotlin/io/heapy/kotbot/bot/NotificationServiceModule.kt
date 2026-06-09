package io.heapy.kotbot.bot

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.markdown.MarkdownModule
import io.heapy.kotbot.infra.KotbotModule

@Module
class NotificationServiceModule(
    private val kotbotModule: KotbotModule,
    private val kotlinChatBotConfigurationModule: KotlinChatBotConfigurationModule,
    private val markdownModule: MarkdownModule,
) {
    val notificationService: NotificationService by lazy {
        NotificationService(
            kotbot = kotbotModule.kotbot,
            chatId = kotlinChatBotConfigurationModule.groupsConfiguration.notificationChannel,
            markdown = markdownModule.markdown,
        )
    }
}
