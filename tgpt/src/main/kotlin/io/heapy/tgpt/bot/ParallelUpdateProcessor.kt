package io.heapy.tgpt.bot

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.model.Update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class ParallelUpdateProcessor(
    private val tgptUpdateProcessor: TgptUpdateProcessor,
    private val applicationScope: CoroutineScope,
) : UpdateProcessor {
    private val updatesChannel = Channel<Update>()

    override fun start() {
        log.info("Starting parallel update processor")

        val channels = mutableMapOf<String, Channel<TypedUpdate>>()
        applicationScope.launch {
            for (update in updatesChannel) {
                val typedUpdate = update.toTypedUpdate() ?: continue

                val channelId: String = when (typedUpdate) {
                    is TypedMessage -> typedUpdate.value.chat.id.toString()
                    is TypedEditedMessage -> typedUpdate.value.chat.id.toString()
                }

                val channel = channels.getOrPut(channelId) {
                    applicationScope.createAndStartChannel()
                }

                channel.send(typedUpdate)
            }
        }
    }

    private fun CoroutineScope.createAndStartChannel(): Channel<TypedUpdate> {
        val updates = Channel<TypedUpdate>()

        launch {
            for (update in updates) {
                try {
                    tgptUpdateProcessor.processUpdate(update)
                } catch (e: Exception) {
                    log.error("Error processing update", e)
                }
            }
        }

        return updates
    }

    override suspend fun processUpdate(update: Update) {
        updatesChannel.send(update)
    }

    private companion object : Logger()
}
