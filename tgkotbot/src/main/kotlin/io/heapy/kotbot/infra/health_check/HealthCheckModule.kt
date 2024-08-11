package io.heapy.kotbot.infra.health_check

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.jdbc.JdbcModule

@Module
open class HealthCheckModule(
    private val jdbcModule: JdbcModule,
) {
    open val pingHealthCheck by lazy {
        PingHealthCheck()
    }

    open val databaseHealthCheck by lazy {
        DatabaseHealthCheck(
            dataSource = jdbcModule.hikariDataSource,
        )
    }

    open val combinedHealthCheck by lazy {
        CombinedHealthCheck(
            healthChecks = listOf(
                pingHealthCheck,
                databaseHealthCheck,
            )
        )
    }

    open val route by lazy {
        HealthCheckRoute(
            healthCheck = combinedHealthCheck,
        )
    }
}
