package io.heapy.kotbot.bot.model

import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * An animated profile photo in the MPEG4 format.
 */
@Serializable
public data class InputProfilePhotoAnimated(
    /**
     * Type of the profile photo, must be *animated*
     */
    public val type: String,
    /**
     * The animated profile photo. Profile photos can't be reused and can only be uploaded as a new file, so you can pass "attach://<file_attach_name>" if the photo was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
     */
    public val animation: String,
    /**
     * *Optional*. Timestamp in seconds of the frame that will be used as the static profile photo. Defaults to 0.0.
     */
    public val main_frame_timestamp: Double? = null,
) : InputProfilePhoto
