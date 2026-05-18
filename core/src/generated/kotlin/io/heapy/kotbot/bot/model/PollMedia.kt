package io.heapy.kotbot.bot.model

import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * At most **one** of the optional fields can be present in any given object.
 */
@Serializable
public data class PollMedia(
    /**
     * *Optional*. Media is an animation, information about the animation
     */
    public val animation: Animation? = null,
    /**
     * *Optional*. Media is an audio file, information about the file; currently, can't be received in a poll option
     */
    public val audio: Audio? = null,
    /**
     * *Optional*. Media is a general file, information about the file; currently, can't be received in a poll option
     */
    public val document: Document? = null,
    /**
     * *Optional*. Media is a live photo, information about the live photo
     */
    public val live_photo: LivePhoto? = null,
    /**
     * *Optional*. Media is a shared location, information about the location
     */
    public val location: Location? = null,
    /**
     * *Optional*. Media is a photo, available sizes of the photo
     */
    public val photo: List<PhotoSize>? = null,
    /**
     * *Optional*. Media is a sticker, information about the sticker; currently, for poll options only
     */
    public val sticker: Sticker? = null,
    /**
     * *Optional*. Media is a venue, information about the venue
     */
    public val venue: Venue? = null,
    /**
     * *Optional*. Media is a video, information about the video
     */
    public val video: Video? = null,
)
