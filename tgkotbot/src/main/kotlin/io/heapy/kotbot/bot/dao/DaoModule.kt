package io.heapy.kotbot.bot.dao

import io.heapy.komok.tech.di.lib.Module

@Module
open class DaoModule {
    open val userContextDao by lazy {
        UserContextDao()
    }

    open val updateDao by lazy {
        UpdateDao()
    }

    open val garbageMessageDao by lazy {
        GarbageMessageDao()
    }

    open val callbackDataDao by lazy {
        CallbackDataDao()
    }

    open val gptSessionDao by lazy {
        GptSessionDao()
    }

    open val jobExecutionDao by lazy {
        JobExecutionDao()
    }
}
