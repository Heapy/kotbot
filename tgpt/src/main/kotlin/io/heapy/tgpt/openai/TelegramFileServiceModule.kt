package io.heapy.tgpt.openai

import io.heapy.komok.tech.di.lib.Module
import io.heapy.tgpt.bot.KotbotModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

@Module
open class TelegramFileServiceModule(
    private val kotbotModule: KotbotModule,
) {
    open val fileDownloadHttpClient by lazy {
        HttpClient(CIO)
    }

    open val telegramFileService by lazy {
        TelegramFileService(
            kotbot = kotbotModule.kotbot,
            httpClient = fileDownloadHttpClient,
            botToken = kotbotModule.botConfiguration.token,
        )
    }
}
