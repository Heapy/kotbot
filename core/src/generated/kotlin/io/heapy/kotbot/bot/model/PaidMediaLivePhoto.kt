package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The paid media is a [live photo](https://core.telegram.org/bots/api/#livephoto).
 */
@Serializable
public data class PaidMediaLivePhoto(
    /**
     * Type of the paid media, always "live_photo"
     */
    public val type: String,
    /**
     * The photo
     */
    public val live_photo: LivePhoto,
) : PaidMedia
