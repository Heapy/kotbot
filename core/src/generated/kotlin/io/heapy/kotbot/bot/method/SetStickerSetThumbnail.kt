package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.Thumbnail
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to set the thumbnail of a regular or mask sticker set. The format of the thumbnail file must match the format of the stickers in the set. Returns *True* on success.
 */
@Serializable
public data class SetStickerSetThumbnail(
    /**
     * Sticker set name
     */
    public val name: String,
    /**
     * User identifier of the sticker set owner
     */
    public val user_id: Long,
    /**
     * A **.WEBP** or **.PNG** image with the thumbnail, must be up to 128 kilobytes in size and have a width and height of exactly 100px, or a **.TGS** animation with a thumbnail up to 32 kilobytes in size (see [https://core.telegram.org/stickers#animated-sticker-requirements](https://core.telegram.org/stickers#animated-sticker-requirements) for animated sticker technical requirements), or a **WEBM** video with the thumbnail up to 32 kilobytes in size; see [https://core.telegram.org/stickers#video-sticker-requirements](https://core.telegram.org/stickers#video-sticker-requirements) for video sticker technical requirements. Pass a *file_id* as a String to send a file that already exists on the Telegram servers, pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files). Animated and video sticker set thumbnails can't be uploaded via HTTP URL. If omitted, then the thumbnail is dropped and the first sticker is used as the thumbnail.
     */
    public val thumbnail: Thumbnail? = null,
    /**
     * Format of the thumbnail, must be one of "static" for a **.WEBP** or **.PNG** image, "animated" for a **.TGS** animation, or "video" for a **WEBM** video
     */
    public val format: String,
) : Method<SetStickerSetThumbnail, Boolean> by Companion {
    public companion object : Method<SetStickerSetThumbnail, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SetStickerSetThumbnail> = serializer()

        override val _name: String = "setStickerSetThumbnail"
    }
}
