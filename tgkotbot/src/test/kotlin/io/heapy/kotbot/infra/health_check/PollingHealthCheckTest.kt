package io.heapy.kotbot.infra.health_check

import io.heapy.kotbot.infra.lifecycle.PollingProbe
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.minutes

class PollingHealthCheckTest {
    @Test
    fun `ok while polling is fresh, nok once it goes stale`() {
        var now = 0L
        val probe = PollingProbe(nanoTime = { now })
        probe.recordPoll() // last successful poll at t=0

        val check = PollingHealthCheck(probe, staleThreshold = 3.minutes)

        now = 2.minutes.inWholeNanoseconds
        assertInstanceOf(HealthCheck.Ok::class.java, check.doCheck())

        now = 4.minutes.inWholeNanoseconds
        assertInstanceOf(HealthCheck.Nok::class.java, check.doCheck())
    }
}
