package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.MessageOrTrue
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to stop updating a live location message before *live_period* expires. On success, if the message is not an inline message, the edited [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is returned.
 */
@Serializable
public data class StopMessageLiveLocation(
    /**
     * Unique identifier of the business connection on behalf of which the message to be edited was sent
     */
    public val business_connection_id: String? = null,
    /**
     * Required if *inline_message_id* is not specified. Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId? = null,
    /**
     * Required if *inline_message_id* is not specified. Identifier of the message with live location to stop
     */
    public val message_id: Int? = null,
    /**
     * Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
     */
    public val inline_message_id: String? = null,
    /**
     * A JSON-serialized object for a new [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
) : Method<StopMessageLiveLocation, MessageOrTrue> by Companion {
    public companion object : Method<StopMessageLiveLocation, MessageOrTrue> {
        override val _deserializer: KSerializer<Response<MessageOrTrue>> =
                Response.serializer(MessageOrTrue.serializer())

        override val _serializer: KSerializer<StopMessageLiveLocation> = serializer()

        override val _name: String = "stopMessageLiveLocation"
    }
}
