package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a video to post as a story.
 */
@Serializable
public data class InputStoryContentVideo(
    /**
     * Type of the content, must be *video*
     */
    public val type: String,
    /**
     * The video to post as a story. The video must be of the size 720x1280, streamable, encoded with H.265 codec, with key frames added each second in the MPEG4 format, and must not exceed 30 MB. The video can't be reused and can only be uploaded as a new file, so you can pass "attach://<file_attach_name>" if the video was uploaded using multipart/form-data under <file_attach_name>. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
     */
    public val video: String,
    /**
     * *Optional*. Precise duration of the video in seconds; 0-60
     */
    public val duration: Double? = null,
    /**
     * *Optional*. Timestamp in seconds of the frame that will be used as the static cover for the story. Defaults to 0.0.
     */
    public val cover_frame_timestamp: Double? = null,
    /**
     * *Optional*. Pass *True* if the video has no sound
     */
    public val is_animation: Boolean? = null,
) : InputStoryContent
