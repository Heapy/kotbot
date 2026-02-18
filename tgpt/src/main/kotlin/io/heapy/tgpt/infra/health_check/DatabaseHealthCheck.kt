package io.heapy.tgpt.infra.health_check

import io.heapy.tgpt.infra.health_check.HealthCheck.HealthResponse
import io.heapy.tgpt.infra.health_check.HealthCheck.Nok
import io.heapy.tgpt.infra.health_check.HealthCheck.Ok
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
