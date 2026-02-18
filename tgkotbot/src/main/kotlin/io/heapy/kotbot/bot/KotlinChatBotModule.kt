package io.heapy.kotbot.bot

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.KotbotModule
import io.heapy.kotbot.infra.lifecycle.ApplicationScopeModule

@Module
open class KotlinChatBotModule(
    private val kotbotModule: KotbotModule,
    private val updateProcessorsModule: UpdateProcessorsModule,
    private val applicationScopeModule: ApplicationScopeModule,
) {
    open val kotlinChatsBot: KotlinChatsBot by lazy {
        KotlinChatsBot(
            kotbot = kotbotModule.kotbot,
            updateProcessor = updateProcessorsModule.updateProcessor,
            applicationScope = applicationScopeModule.applicationScope,
        )
    }
}
