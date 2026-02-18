package io.heapy.kotbot.bot

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.dao.DaoModule
import io.heapy.kotbot.bot.use_case.callback.CallbackDataServiceModule
import io.heapy.kotbot.infra.KotbotModule

@Module
open class CallbackQueryProcessorModule(
    private val callbackDataServiceModule: CallbackDataServiceModule,
    private val kotbotModule: KotbotModule,
    private val userContextServiceModule: UserContextServiceModule,
    private val daoModule: DaoModule,
) {
    open val callbackQueryProcessor by lazy {
        CallbackQueryProcessor(
            callbackDataService = callbackDataServiceModule.callbackDataService,
            kotbot = kotbotModule.kotbot,
            userContextService = userContextServiceModule.userContextService,
            gptSessionDao = daoModule.gptSessionDao,
        )
    }
}
