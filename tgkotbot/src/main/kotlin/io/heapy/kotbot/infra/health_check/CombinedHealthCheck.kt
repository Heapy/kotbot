package io.heapy.kotbot.infra.health_check

import io.heapy.kotbot.infra.health_check.HealthCheck.*

class CombinedHealthCheck(
    private val healthChecks: List<HealthCheck>,
) : HealthCheck {
    override fun doCheck(): HealthResponse {
        val responses = healthChecks.map {
            try {
                it.doCheck()
            } catch (e: Exception) {
                Nok(message = "Health check failed: ${e.message}")
            }
        }
        return if (responses.all { it is Ok }) {
            Ok()
        } else {
            Nok(
                message = responses
                    .filterIsInstance<Nok>()
                    .joinToString(separator = "\n") { it.message }
            )
        }
    }
}
