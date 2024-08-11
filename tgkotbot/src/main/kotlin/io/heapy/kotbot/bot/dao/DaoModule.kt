package io.heapy.kotbot.bot.dao

import io.heapy.komok.tech.di.lib.Module
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
open class DaoModule {
    open val ioDispatcher: CoroutineDispatcher by lazy {
        Dispatchers.IO
    }

    open val userContextDao by lazy {
        UserContextDao(ioDispatcher)
    }

    open val updateDao by lazy {
        UpdateDao(ioDispatcher)
    }
}
