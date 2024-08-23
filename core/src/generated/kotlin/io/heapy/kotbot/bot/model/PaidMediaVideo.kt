package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The paid media is a video.
 */
@Serializable
public data class PaidMediaVideo(
    /**
     * Type of the paid media, always "video"
     */
    public val type: String,
    /**
     * The video
     */
    public val video: Video,
) : PaidMedia
