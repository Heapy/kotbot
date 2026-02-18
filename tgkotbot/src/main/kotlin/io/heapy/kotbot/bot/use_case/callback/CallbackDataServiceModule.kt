package io.heapy.kotbot.bot.use_case.callback

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.dao.DaoModule
import kotlinx.serialization.json.Json

@Module
open class CallbackDataServiceModule(
    private val daoModule: DaoModule,
) {
    open val json by lazy {
        Json
    }

    open val callbackDataService by lazy {
        CallbackDataService(
            callbackDataDao = daoModule.callbackDataDao,
            json = json,
        )
    }
}
