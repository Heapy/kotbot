package io.heapy.tgpt.bot

import io.heapy.komok.tech.di.lib.Module
import io.heapy.tgpt.infra.lifecycle.ApplicationScopeModule

@Module
open class TgptBotModule(
    private val kotbotModule: KotbotModule,
    private val applicationScopeModule: ApplicationScopeModule,
    private val updateProcessorsModule: UpdateProcessorsModule,
) {
    open val tgptBot: TgptBot by lazy {
        TgptBot(
            kotbot = kotbotModule.kotbot,
            updateProcessor = updateProcessorsModule.updateProcessor,
            applicationScope = applicationScopeModule.applicationScope,
        )
    }
}
