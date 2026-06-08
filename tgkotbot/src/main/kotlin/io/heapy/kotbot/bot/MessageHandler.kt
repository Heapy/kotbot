package io.heapy.kotbot.bot

import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.infra.jdbc.TransactionContext

/**
 * Handles a non-command message in priority order. Implementations return `true` when they
 * consume the message (stopping the chain), or `false` to let the next handler try.
 *
 * The ordered list of handlers is assembled in the DI module, so adding a new message feature
 * is a one-line change there — the dispatcher ([KotbotUpdateProcessor]) never has to change.
 */
interface MessageHandler {
    context(_: TransactionContext)
    suspend fun handle(message: Message): Boolean
}