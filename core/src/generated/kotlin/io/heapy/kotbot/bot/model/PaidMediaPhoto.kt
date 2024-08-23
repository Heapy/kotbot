package io.heapy.kotbot.bot.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * The paid media is a photo.
 */
@Serializable
public data class PaidMediaPhoto(
    /**
     * Type of the paid media, always "photo"
     */
    public val type: String,
    /**
     * The photo
     */
    public val photo: List<PhotoSize>,
) : PaidMedia
