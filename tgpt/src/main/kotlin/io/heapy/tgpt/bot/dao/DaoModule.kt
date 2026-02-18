package io.heapy.tgpt.bot.dao

import io.heapy.komok.tech.di.lib.Module

@Module
open class DaoModule {
    open val allowedUserDao by lazy {
        AllowedUserDao()
    }

    open val threadDao by lazy {
        ThreadDao()
    }

    open val threadMessageDao by lazy {
        ThreadMessageDao()
    }

    open val apiCallDao by lazy {
        ApiCallDao()
    }
}
