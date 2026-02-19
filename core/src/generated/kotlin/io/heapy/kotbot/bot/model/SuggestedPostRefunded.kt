package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a service message about a payment refund for a suggested post.
 */
@Serializable
public data class SuggestedPostRefunded(
    /**
     * *Optional*. Message containing the suggested post. Note that the [Message](https://core.telegram.org/bots/api/#message) object in this field will not contain the *reply_to_message* field even if it itself is a reply.
     */
    public val suggested_post_message: Message? = null,
    /**
     * Reason for the refund. Currently, one of "post_deleted" if the post was deleted within 24 hours of being posted or removed from scheduled messages without being posted, or "payment_refunded" if the payer refunded their payment.
     */
    public val reason: String,
)
