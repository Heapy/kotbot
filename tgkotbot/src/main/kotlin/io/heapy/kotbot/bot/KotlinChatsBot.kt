package io.heapy.kotbot.bot

import io.heapy.komok.tech.logging.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.system.exitProcess

class KotlinChatsBot(
    private val kotbot: Kotbot,
    private val updateProcessor: UpdateProcessor,
    private val applicationScope: CoroutineScope,
) {
    /**
     * Start receiving updates and processing them.
     * In case of any error, the bot will be restarted.
     */
    fun start() {
        kotbot.receiveUpdates()
            .onEach { update ->
                updateProcessor.processUpdate(update)
            }
            .launchIn(applicationScope)
            .invokeOnCompletion { cause ->
                if (cause != null) {
                    log.error("Receive Updates flow failed", cause)
                    exitProcess(1)
                }
            }
    }

    private companion object : Logger()
}
