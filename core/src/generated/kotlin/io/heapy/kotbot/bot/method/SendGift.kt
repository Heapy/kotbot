package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.MessageEntity
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Sends a gift to the given user. The gift can't be converted to Telegram Stars by the user. Returns *True* on success.
 */
@Serializable
public data class SendGift(
    /**
     * Unique identifier of the target user that will receive the gift
     */
    public val user_id: Long,
    /**
     * Identifier of the gift
     */
    public val gift_id: String,
    /**
     * Text that will be shown along with the gift; 0-255 characters
     */
    public val text: String? = null,
    /**
     * Mode for parsing entities in the text. See [formatting options](https://core.telegram.org/bots/api/#formatting-options) for more details. Entities other than "bold", "italic", "underline", "strikethrough", "spoiler", and "custom_emoji" are ignored.
     */
    public val text_parse_mode: String? = null,
    /**
     * A JSON-serialized list of special entities that appear in the gift text. It can be specified instead of *text_parse_mode*. Entities other than "bold", "italic", "underline", "strikethrough", "spoiler", and "custom_emoji" are ignored.
     */
    public val text_entities: List<MessageEntity>? = null,
) : Method<SendGift, Boolean> by Companion {
    public companion object : Method<SendGift, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<SendGift> = serializer()

        override val _name: String = "sendGift"
    }
}
