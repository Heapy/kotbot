package io.heapy.kotbot.infra.health_check

import io.heapy.komok.tech.di.lib.Module
import io.heapy.kotbot.infra.jdbc.JdbcModule
import io.heapy.kotbot.infra.lifecycle.DEFAULT_POLL_STALE_THRESHOLD
import io.heapy.kotbot.infra.lifecycle.PollingProbeModule

@Module
class HealthCheckModule(
    private val jdbcModule: JdbcModule,
    private val pollingProbeModule: PollingProbeModule,
) {
    val pingHealthCheck by lazy {
        PingHealthCheck()
    }

    val databaseHealthCheck by lazy {
        DatabaseHealthCheck(
            dataSource = jdbcModule.hikariDataSource,
        )
    }

    val pollingHealthCheck by lazy {
        PollingHealthCheck(
            pollingProbe = pollingProbeModule.pollingProbe,
            staleThreshold = DEFAULT_POLL_STALE_THRESHOLD,
        )
    }

    val combinedHealthCheck by lazy {
        CombinedHealthCheck(
            healthChecks = listOf(
                pingHealthCheck,
                databaseHealthCheck,
                pollingHealthCheck,
            )
        )
    }

    val route by lazy {
        HealthCheckRoute(
            healthCheck = combinedHealthCheck,
        )
    }
}
