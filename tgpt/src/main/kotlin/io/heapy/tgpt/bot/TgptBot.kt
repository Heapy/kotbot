package io.heapy.tgpt.bot

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.receiveUpdates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.system.exitProcess

class TgptBot(
    private val kotbot: Kotbot,
    private val updateProcessor: UpdateProcessor,
    private val applicationScope: CoroutineScope,
) {
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
