package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The boost was obtained by the creation of a Telegram Premium giveaway. This boosts the chat 4 times for the duration of the corresponding Telegram Premium subscription.
 */
@Serializable
public data class ChatBoostSourceGiveaway(
    /**
     * Source of the boost, always "giveaway"
     */
    public val source: String = "giveaway",
    /**
     * Identifier of a message in the chat with the giveaway; the message could have been deleted already. May be 0 if the message isn't sent yet.
     */
    public val giveaway_message_id: Int,
    /**
     * *Optional*. User that won the prize in the giveaway if any
     */
    public val user: User? = null,
    /**
     * *Optional*. True, if the giveaway was completed, but there was no user to win the prize
     */
    public val is_unclaimed: Boolean? = true,
) : ChatBoostSource
