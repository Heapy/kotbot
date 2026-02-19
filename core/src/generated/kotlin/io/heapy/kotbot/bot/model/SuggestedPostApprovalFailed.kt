package io.heapy.kotbot.bot.model

import kotlinx.serialization.Serializable

/**
 * Describes a service message about the failed approval of a suggested post. Currently, only caused by insufficient user funds at the time of approval.
 */
@Serializable
public data class SuggestedPostApprovalFailed(
    /**
     * *Optional*. Message containing the suggested post whose approval has failed. Note that the [Message](https://core.telegram.org/bots/api/#message) object in this field will not contain the *reply_to_message* field even if it itself is a reply.
     */
    public val suggested_post_message: Message? = null,
    /**
     * Expected price of the post
     */
    public val price: SuggestedPostPrice,
)
