package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a [video message](https://telegram.org/blog/video-messages-and-telescope) (available in Telegram apps as of [v.4.0](https://telegram.org/blog/video-messages-and-telescope)).
 */
@Serializable
public data class VideoNote(
    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    public val file_id: String,
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
     */
    public val file_unique_id: String,
    /**
     * Video width and height (diameter of the video message) as defined by sender
     */
    public val length: Int,
    /**
     * Duration of the video in seconds as defined by sender
     */
    public val duration: Int,
    /**
     * *Optional*. Video thumbnail
     */
    public val thumb: PhotoSize? = null,
    /**
     * *Optional*. File size in bytes
     */
    public val file_size: Long? = null,
)
