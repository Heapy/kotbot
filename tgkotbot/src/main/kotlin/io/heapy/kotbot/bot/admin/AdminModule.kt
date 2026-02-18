package io.heapy.kotbot.bot.admin

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.dao.DaoModule
import io.heapy.kotbot.infra.jdbc.JdbcModule

@Module
open class AdminModule(
    private val daoModule: DaoModule,
    private val jdbcModule: JdbcModule,
) {
    open val route by lazy {
        AdminRoute(
            userContextDao = daoModule.userContextDao,
            transactionProvider = jdbcModule.transactionProvider,
        )
    }
}
