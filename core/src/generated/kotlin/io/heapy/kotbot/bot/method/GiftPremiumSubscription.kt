package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.MessageEntity
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

/**
 * Gifts a Telegram Premium subscription to the given user. Returns *True* on success.
 */
@Serializable
public data class GiftPremiumSubscription(
    /**
     * Unique identifier of the target user who will receive a Telegram Premium subscription
     */
    public val user_id: Long,
    /**
     * Number of months the Telegram Premium subscription will be active for the user; must be one of 3, 6, or 12
     */
    public val month_count: Int,
    /**
     * Number of Telegram Stars to pay for the Telegram Premium subscription; must be 1000 for 3 months, 1500 for 6 months, and 2500 for 12 months
     */
    public val star_count: Int,
    /**
     * Text that will be shown along with the service message about the subscription; 0-128 characters
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
) : Method<GiftPremiumSubscription, Boolean> by Companion {
    public companion object : Method<GiftPremiumSubscription, Boolean> {
        override val _deserializer: KSerializer<Response<Boolean>> =
                Response.serializer(Boolean.serializer())

        override val _serializer: KSerializer<GiftPremiumSubscription> = serializer()

        override val _name: String = "giftPremiumSubscription"
    }
}
