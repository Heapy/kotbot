package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a transaction with a chat.
 */
@Serializable
public data class TransactionPartnerChat(
    /**
     * Type of the transaction partner, always "chat"
     */
    public val type: String,
    /**
     * Information about the chat
     */
    public val chat: Chat,
    /**
     * *Optional*. The gift sent to the chat by the bot
     */
    public val gift: Gift? = null,
) : TransactionPartner
