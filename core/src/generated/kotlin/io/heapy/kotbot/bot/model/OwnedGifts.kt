package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Contains the list of gifts received and owned by a user or a chat.
 */
@Serializable
public data class OwnedGifts(
    /**
     * The total number of gifts owned by the user or the chat
     */
    public val total_count: Int,
    /**
     * The list of gifts
     */
    public val gifts: List<OwnedGift>,
    /**
     * *Optional*. Offset for the next request. If empty, then there are no more results
     */
    public val next_offset: String? = null,
)
