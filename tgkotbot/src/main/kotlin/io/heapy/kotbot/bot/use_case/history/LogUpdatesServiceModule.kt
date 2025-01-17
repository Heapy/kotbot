package io.heapy.kotbot.bot.use_case.history

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.bot.dao.DaoModule
import io.heapy.kotbot.infra.jdbc.JdbcModule
import io.heapy.kotbot.infra.lifecycle.ApplicationScopeModule
import io.heapy.kotbot.infra.metrics.MetricsModule

@Module
open class LogUpdatesServiceModule(
    private val daoModule: DaoModule,
    private val jdbcModule: JdbcModule,
    private val applicationScopeModule: ApplicationScopeModule,
    private val metricsModule: MetricsModule,
) {
    open val logUpdatesService by lazy {
        LogUpdatesService(
            updateDao = daoModule.updateDao,
            transactionProvider = jdbcModule.transactionProvider,
            applicationScope = applicationScopeModule.applicationScope,
            meterRegistry = metricsModule.meterRegistry,
        )
    }
}
