package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.OwnedGifts
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Returns the gifts owned by a chat. Returns [OwnedGifts](https://core.telegram.org/bots/api/#ownedgifts) on success.
 */
@Serializable
public data class GetChatGifts(
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Pass *True* to exclude gifts that aren't saved to the chat's profile page. Always *True*, unless the bot has the *can_post_messages* administrator right in the channel.
     */
    public val exclude_unsaved: Boolean? = null,
    /**
     * Pass *True* to exclude gifts that are saved to the chat's profile page. Always *False*, unless the bot has the *can_post_messages* administrator right in the channel.
     */
    public val exclude_saved: Boolean? = null,
    /**
     * Pass *True* to exclude gifts that can be purchased an unlimited number of times
     */
    public val exclude_unlimited: Boolean? = null,
    /**
     * Pass *True* to exclude gifts that can be purchased a limited number of times and can be upgraded to unique
     */
    public val exclude_limited_upgradable: Boolean? = null,
    /**
     * Pass *True* to exclude gifts that can be purchased a limited number of times and can't be upgraded to unique
     */
    public val exclude_limited_non_upgradable: Boolean? = null,
    /**
     * Pass *True* to exclude gifts that were assigned from the TON blockchain and can't be resold or transferred in Telegram
     */
    public val exclude_from_blockchain: Boolean? = null,
    /**
     * Pass *True* to exclude unique gifts
     */
    public val exclude_unique: Boolean? = null,
    /**
     * Pass *True* to sort results by gift price instead of send date. Sorting is applied before pagination.
     */
    public val sort_by_price: Boolean? = null,
    /**
     * Offset of the first entry to return as received from the previous request; use an empty string to get the first chunk of results
     */
    public val offset: String? = null,
    /**
     * The maximum number of gifts to be returned; 1-100. Defaults to 100
     */
    public val limit: Int? = null,
) : Method<GetChatGifts, OwnedGifts> by Companion {
    public companion object : Method<GetChatGifts, OwnedGifts> {
        override val _deserializer: KSerializer<Response<OwnedGifts>> =
                Response.serializer(OwnedGifts.serializer())

        override val _serializer: KSerializer<GetChatGifts> = serializer()

        override val _name: String = "getChatGifts"
    }
}
