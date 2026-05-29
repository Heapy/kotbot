package io.heapy.kotbot.infra.health_check

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.jdbc.JdbcModule
import io.heapy.kotbot.infra.lifecycle.DEFAULT_POLL_STALE_THRESHOLD
import io.heapy.kotbot.infra.lifecycle.PollingProbeModule

@Module
open class HealthCheckModule(
    private val jdbcModule: JdbcModule,
    private val pollingProbeModule: PollingProbeModule,
) {
    open val pingHealthCheck by lazy {
        PingHealthCheck()
    }

    open val databaseHealthCheck by lazy {
        DatabaseHealthCheck(
            dataSource = jdbcModule.hikariDataSource,
        )
    }

    open val pollingHealthCheck by lazy {
        PollingHealthCheck(
            pollingProbe = pollingProbeModule.pollingProbe,
            staleThreshold = DEFAULT_POLL_STALE_THRESHOLD,
        )
    }

    open val combinedHealthCheck by lazy {
        CombinedHealthCheck(
            healthChecks = listOf(
                pingHealthCheck,
                databaseHealthCheck,
                pollingHealthCheck,
            )
        )
    }

    open val route by lazy {
        HealthCheckRoute(
            healthCheck = combinedHealthCheck,
        )
    }
}
