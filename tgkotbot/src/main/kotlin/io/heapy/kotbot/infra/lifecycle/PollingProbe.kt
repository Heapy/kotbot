package io.heapy.kotbot.infra.lifecycle

import io.heapy.komok.tech.di.lib.Module
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds

/**
 * How long the bot may go without a successful Telegram poll before it is considered stalled.
 *
 * A healthy bot polls at least every ~50s (the long-poll timeout), so this is set
 * well above that to avoid false positives during transient hiccups.
 */
val DEFAULT_POLL_STALE_THRESHOLD: Duration = 3.minutes

/**
 * Tracks the time of the last successful Telegram poll so liveness can be observed.
 *
 * [recordPoll] is called by the collector of `receiveUpdates` on every emitted batch (one
 * per successful poll, including empty ones); [sinceLastPoll] is read by the health check and
 * the watchdog. Uses a monotonic clock so it is immune to wall-clock adjustments. [nanoTime]
 * is injectable for tests.
 */
class PollingProbe(
    private val nanoTime: () -> Long = System::nanoTime,
) {
    @Volatile
    private var lastPollNanos: Long = nanoTime()

    fun recordPoll() {
        lastPollNanos = nanoTime()
    }

    fun sinceLastPoll(): Duration =
        (nanoTime() - lastPollNanos).nanoseconds
}

@Module
class PollingProbeModule {
    val pollingProbe by lazy {
        PollingProbe()
    }
}
