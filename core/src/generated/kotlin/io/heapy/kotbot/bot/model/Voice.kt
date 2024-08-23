package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a voice note.
 */
@Serializable
public data class Voice(
    /**
     * Identifier for this file, which can be used to download or reuse the file
     */
    public val file_id: String,
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
     */
    public val file_unique_id: String,
    /**
     * Duration of the audio in seconds as defined by the sender
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
