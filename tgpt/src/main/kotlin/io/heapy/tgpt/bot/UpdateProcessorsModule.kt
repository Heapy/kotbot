package io.heapy.tgpt.bot

import io.heapy.komok.tech.di.lib.Module
import io.heapy.tgpt.bot.dao.DaoModule
import io.heapy.tgpt.infra.jdbc.JdbcModule
import io.heapy.tgpt.infra.lifecycle.ApplicationScopeModule
import io.heapy.tgpt.openai.OpenAiModule
import io.heapy.tgpt.openai.TelegramFileServiceModule

@Module
open class UpdateProcessorsModule(
    private val daoModule: DaoModule,
    private val jdbcModule: JdbcModule,
    private val applicationScopeModule: ApplicationScopeModule,
    private val kotbotModule: KotbotModule,
    private val openAiModule: OpenAiModule,
    private val telegramFileServiceModule: TelegramFileServiceModule,
) {
    open val tgptUpdateProcessor: TgptUpdateProcessor by lazy {
        TgptUpdateProcessor(
            kotbot = kotbotModule.kotbot,
            openAiService = openAiModule.openAiService,
            telegramFileService = telegramFileServiceModule.telegramFileService,
            allowedUserDao = daoModule.allowedUserDao,
            threadDao = daoModule.threadDao,
            threadMessageDao = daoModule.threadMessageDao,
            apiCallDao = daoModule.apiCallDao,
            transactionProvider = jdbcModule.transactionProvider,
        )
    }

    open val updateProcessor: UpdateProcessor by lazy {
        ParallelUpdateProcessor(
            tgptUpdateProcessor = tgptUpdateProcessor,
            applicationScope = applicationScopeModule.applicationScope,
        )
    }
}
