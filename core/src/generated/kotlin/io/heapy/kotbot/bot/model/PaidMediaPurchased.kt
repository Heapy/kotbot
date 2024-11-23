package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object contains information about a paid media purchase.
 */
@Serializable
public data class PaidMediaPurchased(
    /**
     * User who purchased the media
     */
    public val from: User,
    /**
     * Bot-specified paid media payload
     */
    public val paid_media_payload: String,
)
