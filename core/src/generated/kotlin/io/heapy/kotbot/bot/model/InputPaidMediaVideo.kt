package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The paid media to send is a video.
 */
@Serializable
public data class InputPaidMediaVideo(
    /**
     * Type of the media, must be *video*
     */
    public val type: String,
    /**
     * File to send. Pass a file_id to send a file that exists on the Telegram servers (recommended), pass an HTTP URL for Telegram to get a file from the Internet, or pass "attach://<file_attach_name>" to upload a new one using multipart/form-data under <file_attach_name> name. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
     */
    public val media: String,
    /**
     * *Optional*. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file, so you can pass "attach://<file_attach_name>" if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
     */
    public val thumbnail: Thumbnail? = null,
    /**
     * *Optional*. Video width
     */
    public val width: Int? = null,
    /**
     * *Optional*. Video height
     */
    public val height: Int? = null,
    /**
     * *Optional*. Video duration in seconds
     */
    public val duration: Int? = null,
    /**
     * *Optional*. Pass *True* if the uploaded video is suitable for streaming
     */
    public val supports_streaming: Boolean? = null,
) : InputPaidMedia
