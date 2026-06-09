package io.heapy.kotbot.bot.dao

import io.heapy.komok.tech.di.lib.Module

@Module
open class DaoModule {
    val userContextDao by lazy {
        UserContextDao()
    }

    val updateDao by lazy {
        UpdateDao()
    }

    val garbageMessageDao by lazy {
        GarbageMessageDao()
    }

    open val callbackDataDao by lazy {
        CallbackDataDao()
    }

    val gptSessionDao by lazy {
        GptSessionDao()
    }

    val jobExecutionDao by lazy {
        JobExecutionDao()
    }
}
