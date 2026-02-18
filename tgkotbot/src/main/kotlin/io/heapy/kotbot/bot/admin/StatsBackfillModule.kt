package io.heapy.kotbot.bot.admin

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.dao.DaoModule
import io.heapy.kotbot.infra.jdbc.JdbcModule
import io.heapy.kotbot.infra.lifecycle.ApplicationScopeModule

@Module
open class StatsBackfillModule(
    private val daoModule: DaoModule,
    private val jdbcModule: JdbcModule,
    private val applicationScopeModule: ApplicationScopeModule,
) {
    open val statsBackfillJob by lazy {
        StatsBackfillJob(
            userContextDao = daoModule.userContextDao,
            updateDao = daoModule.updateDao,
            transactionProvider = jdbcModule.transactionProvider,
            applicationScope = applicationScopeModule.applicationScope,
        )
    }
}
