package io.heapy.tgpt.bot.admin

import io.heapy.komok.tech.di.lib.Module
import io.heapy.tgpt.bot.dao.DaoModule
import io.heapy.tgpt.infra.jdbc.JdbcModule

@Module
open class AdminModule(
    private val daoModule: DaoModule,
    private val jdbcModule: JdbcModule,
) {
    open val route by lazy {
        AdminRoute(
            allowedUserDao = daoModule.allowedUserDao,
            apiCallDao = daoModule.apiCallDao,
            threadDao = daoModule.threadDao,
            threadMessageDao = daoModule.threadMessageDao,
            transactionProvider = jdbcModule.transactionProvider,
        )
    }
}
