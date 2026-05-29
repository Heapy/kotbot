package io.heapy.kotbot.bot

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.infra.lifecycle.PollingProbe
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.system.exitProcess

class KotlinChatsBot(
    private val kotbot: Kotbot,
    private val updateProcessor: UpdateProcessor,
    private val applicationScope: CoroutineScope,
    private val pollingProbe: PollingProbe,
) {
    /**
     * Start receiving updates and processing them.
     */
    fun start() {
        kotbot.receiveUpdates()
            .onEach { updates ->
                pollingProbe.recordPoll()
                updates.forEach { update ->
                    updateProcessor.processUpdate(update)
                }
            }
            .launchIn(applicationScope)
            .invokeOnCompletion { cause ->
                when (cause) {
                    is CancellationException ->
                        log.info("Receive Updates flow stopped (shutdown)")
                    else -> {
                        log.error("Receive Updates flow terminated unexpectedly, exiting for restart", cause)
                        exitProcess(1)
                    }
                }
            }
    }

    private companion object : Logger()
}
