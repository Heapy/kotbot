package io.heapy.kotbot.infra.health_check

import io.heapy.kotbot.infra.health_check.HealthCheck.HealthResponse
import io.heapy.kotbot.infra.health_check.HealthCheck.Nok
import io.heapy.kotbot.infra.health_check.HealthCheck.Ok
import io.heapy.kotbot.infra.lifecycle.PollingProbe
import kotlin.time.Duration

/**
 * Reports unhealthy when the bot hasn't successfully polled Telegram within [staleThreshold].
 *
 * Makes the "process alive but not receiving updates" zombie state observable on `/healthcheck`
 * (returns `503`), so an orchestrator or autoheal sidecar can restart the container..
 */
class PollingHealthCheck(
    private val pollingProbe: PollingProbe,
    private val staleThreshold: Duration,
) : HealthCheck {
    override fun doCheck(): HealthResponse {
        val sinceLastPoll = pollingProbe.sinceLastPoll()
        return if (sinceLastPoll <= staleThreshold) {
            Ok()
        } else {
            Nok(message = "No successful Telegram poll for $sinceLastPoll (threshold $staleThreshold)")
        }
    }
}
