package io.heapy.kotbot.bot

import io.heapy.komok.tech.logging.Logger
import io.heapy.kotbot.bot.model.Update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class ParallelUpdateProcessor(
    private val typedUpdateProcessor: TypedUpdateProcessor,
    private val applicationScope: CoroutineScope,
) : UpdateProcessor {
    private val updatesChannel = Channel<Update>()

    override fun start() {
        log.info("Starting parallel update processor")

        val channels = mutableMapOf<String, Channel<TypedUpdate>>()
        applicationScope.launch {
            for (update in updatesChannel) {
                val typedUpdate = update.toTypedUpdate()

                val channelId: String = when (typedUpdate) {
                    is TypedMessage -> typedUpdate.value.chat.id.toString()
                    is TypedEditedMessage -> typedUpdate.value.chat.id.toString()
                    is TypedChannelPost -> typedUpdate.value.chat.id.toString()
                    is TypedEditedChannelPost -> typedUpdate.value.chat.id.toString()
                    is TypedBusinessConnection -> typedUpdate.value.user_chat_id.toString()
                    is TypedBusinessMessage -> typedUpdate.value.chat.id.toString()
                    is TypedEditedBusinessMessage -> typedUpdate.value.chat.id.toString()
                    is TypedDeletedBusinessMessages -> typedUpdate.value.chat.id.toString()
                    is TypedMessageReaction -> typedUpdate.value.chat.id.toString()
                    is TypedMessageReactionCount -> typedUpdate.value.chat.id.toString()
                    is TypedInlineQuery -> typedUpdate.value.from.id.toString()
                    is TypedChosenInlineResult -> typedUpdate.value.from.id.toString()
                    is TypedCallbackQuery -> typedUpdate.value.from.id.toString()
                    is TypedShippingQuery -> typedUpdate.value.from.id.toString()
                    is TypedPreCheckoutQuery -> typedUpdate.value.from.id.toString()
                    is TypedPoll -> typedUpdate.value.id
                    is TypedPollAnswer -> typedUpdate.value.poll_id
                    is TypedMyChatMember -> typedUpdate.value.chat.id.toString()
                    is TypedChatMember -> typedUpdate.value.chat.id.toString()
                    is TypedChatJoinRequest -> typedUpdate.value.chat.id.toString()
                    is TypedChatBoost -> typedUpdate.value.chat.id.toString()
                    is TypedRemovedChatBoost -> typedUpdate.value.chat.id.toString()
                    is TypedPurchasedPaidMedia -> typedUpdate.value.from.id.toString()
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
                    typedUpdateProcessor.processUpdate(update)
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
