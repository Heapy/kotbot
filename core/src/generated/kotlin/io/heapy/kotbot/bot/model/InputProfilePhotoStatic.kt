package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A static profile photo in the .JPG format.
 */
@Serializable
public data class InputProfilePhotoStatic(
    /**
     * Type of the profile photo, must be *static*
     */
    public val type: String,
    /**
     * The static profile photo. Profile photos can't be reused and can only be uploaded as a new file, so you can pass "attach://<file_attach_name>" if the photo was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
     */
    public val photo: String,
) : InputProfilePhoto
