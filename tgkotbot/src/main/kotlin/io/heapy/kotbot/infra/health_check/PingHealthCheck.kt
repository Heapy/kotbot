package io.heapy.kotbot.infra.health_check

import io.heapy.kotbot.infra.health_check.HealthCheck.*

class PingHealthCheck : HealthCheck {
    override fun doCheck(): HealthResponse {
        return Ok()
    }
}
