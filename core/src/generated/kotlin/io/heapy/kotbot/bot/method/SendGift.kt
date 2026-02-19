package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.MessageEntity
import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Sends a gift to the given user or channel chat. The gift can't be converted to Telegram Stars by the receiver. Returns *True* on success.
 */
@Serializable
public data class SendGift(
    /**
     * Required if *chat_id* is not specified. Unique identifier of the target user who will receive the gift.
     */
    public val user_id: Long? = null,
    /**
     * Required if *user_id* is not specified. Unique identifier for the chat or username of the channel (in the format `@channelusername`) that will receive the gift.
     */
    public val chat_id: ChatId? = null,
    /**
     * Identifier of the gift; limited gifts can't be sent to channel chats
     */
    public val gift_id: String,
    /**
     * Pass *True* to pay for the gift upgrade from the bot's balance, thereby making the upgrade free for the receiver
     */
    public val pay_for_upgrade: Boolean? = null,
    /**
     * Text that will be shown along with the gift; 0-128 characters
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
