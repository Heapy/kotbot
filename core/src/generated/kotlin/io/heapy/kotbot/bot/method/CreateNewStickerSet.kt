package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Kotbot
import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.InputFile
import io.heapy.kotbot.bot.model.MaskPosition
import io.heapy.kotbot.bot.requestForJson
import io.heapy.kotbot.bot.unwrap
import io.ktor.client.statement.bodyAsText
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to create a new sticker set owned by a user. The bot will be able to edit the sticker set thus created. You **must** use exactly one of the fields *png_sticker*, *tgs_sticker*, or *webm_sticker*. Returns *True* on success.
 */
@Serializable
public data class CreateNewStickerSet(
    /**
     * User identifier of created sticker set owner
     */
    public val user_id: Long,
    /**
     * Short name of sticker set, to be used in `t.me/addstickers/` URLs (e.g., *animals*). Can contain only English letters, digits and underscores. Must begin with a letter, can't contain consecutive underscores and must end in `"_by_<bot_username>"`. `<bot_username>` is case insensitive. 1-64 characters.
     */
    public val name: String,
    /**
     * Sticker set title, 1-64 characters
     */
    public val title: String,
    /**
     * **PNG** image with the sticker, must be up to 512 kilobytes in size, dimensions must not exceed 512px, and either width or height must be exactly 512px. Pass a *file_id* as a String to send a file that already exists on the Telegram servers, pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. [More information on Sending Files &raquo;](https://core.telegram.org/bots/api/#sending-files)
     */
    public val png_sticker: InputFile? = null,
    /**
     * **TGS** animation with the sticker, uploaded using multipart/form-data. See [https://core.telegram.org/stickers#animated-sticker-requirements](https://core.telegram.org/stickers#animated-sticker-requirements) for technical requirements
     */
    public val tgs_sticker: InputFile? = null,
    /**
     * **WEBM** video with the sticker, uploaded using multipart/form-data. See [https://core.telegram.org/stickers#video-sticker-requirements](https://core.telegram.org/stickers#video-sticker-requirements) for technical requirements
     */
    public val webm_sticker: InputFile? = null,
    /**
     * Type of stickers in the set, pass "regular" or "mask". Custom emoji sticker sets can't be created via the Bot API at the moment. By default, a regular sticker set is created.
     */
    public val sticker_type: String? = null,
    /**
     * One or more emoji corresponding to the sticker
     */
    public val emojis: String,
    /**
     * A JSON-serialized object for position where the mask should be placed on faces
     */
    public val mask_position: MaskPosition? = null,
) : Method<Boolean> {
    public override suspend fun Kotbot.execute(): Boolean = requestForJson(
        name = "createNewStickerSet",
        serialize = {
            json.encodeToString(
                serializer(),
                this@CreateNewStickerSet
            )
        },
        deserialize = {
            json.decodeFromString(deserializer, it.bodyAsText()).unwrap()
        },
    )

    public companion object {
        public val deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())
    }
}
