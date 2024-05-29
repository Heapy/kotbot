package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.InputSticker
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Use this method to create a new sticker set owned by a user. The bot will be able to edit the sticker set thus created. Returns *True* on success.
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
     * A JSON-serialized list of 1-50 initial stickers to be added to the sticker set
     */
    public val stickers: List<InputSticker>,
    /**
     * Type of stickers in the set, pass "regular", "mask", or "custom_emoji". By default, a regular sticker set is created.
     */
    public val sticker_type: String? = null,
    /**
     * Pass *True* if stickers in the sticker set must be repainted to the color of text when used in messages, the accent color if used as emoji status, white on chat photos, or another appropriate color based on context; for custom emoji sticker sets only
     */
    public val needs_repainting: Boolean? = null,
) : Method<CreateNewStickerSet, Boolean> by Companion {
    public companion object : Method<CreateNewStickerSet, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<CreateNewStickerSet> = serializer()

        override val _name: String = "createNewStickerSet"
    }
}
