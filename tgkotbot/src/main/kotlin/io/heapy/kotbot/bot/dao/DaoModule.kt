package io.heapy.kotbot.bot.dao

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.jdbc.JdbcModule

@Module
open class DaoModule(
    private val jdbcModule: JdbcModule,
) {
    open val jooqDao: JooqDao by lazy {
        DefaultJooqDao(
            dslContext = jdbcModule.dslContext,
        )
    }

    open val userContextDao by lazy {
        UserContextDao(
            jooqDao = jooqDao,
        )
    }

    open val updateDao by lazy {
        UpdateDao(
            jooqDao = jooqDao,
        )
    }

    open val garbageMessageDao by lazy {
        GarbageMessageDao(
            jooqDao = jooqDao,
        )
    }
}
