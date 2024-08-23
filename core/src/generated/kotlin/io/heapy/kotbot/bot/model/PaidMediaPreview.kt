package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The paid media isn't available before the payment.
 */
@Serializable
public data class PaidMediaPreview(
    /**
     * Type of the paid media, always "preview"
     */
    public val type: String,
    /**
     * *Optional*. Media width as defined by the sender
     */
    public val width: Int? = null,
    /**
     * *Optional*. Media height as defined by the sender
     */
    public val height: Int? = null,
    /**
     * *Optional*. Duration of the media in seconds as defined by the sender
     */
    public val duration: Int? = null,
) : PaidMedia
