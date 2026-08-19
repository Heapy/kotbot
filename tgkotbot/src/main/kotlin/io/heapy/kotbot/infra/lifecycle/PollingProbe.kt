package io.heapy.kotbot.infra.lifecycle

import io.heapy.komok.tech.di.lib.Module
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds

/**
 * How long the bot may go without a successful Telegram poll before it is considered stalled.
 *
 * Sized against a run of failed polls, not against a healthy one. A healthy poll returns in ~50s,
 * but a poll that misses its deadline costs 61s (the 60s client deadline plus the 1s retry delay),
 * and those arrive in bursts: Telegram answers late often enough that a fifth to a third of polls
 * miss the deadline on a bad day. At the previous value of 3 minutes a mere three consecutive
 * misses -- 183s -- already tripped the health check, so the autoheal sidecar restarted a process
 * that would have recovered by itself on the next poll. That cost 7-18 restarts a day.
 *
 * Five minutes rides out four consecutive misses while still catching a genuinely stalled poller
 * well inside ten minutes.
 */
val DEFAULT_POLL_STALE_THRESHOLD: Duration = 5.minutes

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
