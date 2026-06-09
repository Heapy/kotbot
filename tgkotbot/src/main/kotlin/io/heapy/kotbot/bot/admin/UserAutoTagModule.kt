package io.heapy.kotbot.bot.admin

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.dao.DaoModule
import io.heapy.kotbot.infra.jdbc.JdbcModule
import io.heapy.kotbot.infra.lifecycle.ApplicationScopeModule

@Module
class UserAutoTagModule(
    private val daoModule: DaoModule,
    private val jdbcModule: JdbcModule,
    private val applicationScopeModule: ApplicationScopeModule,
) {
    val userAutoTagJob by lazy {
        UserAutoTagJob(
            userContextDao = daoModule.userContextDao,
            jobExecutionDao = daoModule.jobExecutionDao,
            transactionProvider = jdbcModule.transactionProvider,
            applicationScope = applicationScopeModule.applicationScope,
        )
    }
}
