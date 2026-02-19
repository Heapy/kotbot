package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Contains information about a suggested post.
 */
@Serializable
public data class SuggestedPostInfo(
    /**
     * State of the suggested post. Currently, it can be one of "pending", "approved", "declined".
     */
    public val state: String,
    /**
     * *Optional*. Proposed price of the post. If the field is omitted, then the post is unpaid.
     */
    public val price: SuggestedPostPrice? = null,
    /**
     * *Optional*. Proposed send date of the post. If the field is omitted, then the post can be published at any time within 30 days at the sole discretion of the user or administrator who approves it.
     */
    public val send_date: Int? = null,
)
