package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represents a live photo.
 */
@Serializable
public data class LivePhoto(
    /**
     * *Optional*. Available sizes of the corresponding static photo
     */
    public val photo: List<PhotoSize>? = null,
    /**
     * Identifier for the video file which can be used to download or reuse the file
     */
    public val file_id: String,
    /**
     * Unique identifier for the video file which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
     */
    public val file_unique_id: String,
    /**
     * Video width as defined by the sender
     */
    public val width: Int,
    /**
     * Video height as defined by the sender
     */
    public val height: Int,
    /**
     * Duration of the video in seconds as defined by the sender
     */
    public val duration: Int,
    /**
     * *Optional*. MIME type of the file as defined by the sender
     */
    public val mime_type: String? = null,
    /**
     * *Optional*. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
     */
    public val file_size: Long? = null,
)
