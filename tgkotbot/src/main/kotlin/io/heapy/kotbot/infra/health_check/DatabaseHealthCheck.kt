package io.heapy.kotbot.infra.health_check

import io.heapy.kotbot.infra.health_check.HealthCheck.*
import javax.sql.DataSource

class DatabaseHealthCheck(
    private val dataSource: DataSource,
) : HealthCheck {
    override fun doCheck(): HealthResponse {
        return dataSource.connection.use {
            if (it.isValid(5)) {
                Ok()
            } else {
                Nok(message = "Database is not available")
            }
        }
    }
}
