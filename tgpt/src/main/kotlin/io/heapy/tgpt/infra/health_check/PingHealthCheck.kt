package io.heapy.tgpt.infra.health_check

import io.heapy.tgpt.infra.health_check.HealthCheck.HealthResponse
import io.heapy.tgpt.infra.health_check.HealthCheck.Ok

class PingHealthCheck : HealthCheck {
    override fun doCheck(): HealthResponse {
        return Ok()
    }
}
