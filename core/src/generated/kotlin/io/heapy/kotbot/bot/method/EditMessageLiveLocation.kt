package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.InlineKeyboardMarkup
import io.heapy.kotbot.bot.model.MessageOrTrue
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to edit live location messages. A location can be edited until its *live_period* expires or editing is explicitly disabled by a call to [stopMessageLiveLocation](https://core.telegram.org/bots/api/#stopmessagelivelocation). On success, if the edited message is not an inline message, the edited [Message](https://core.telegram.org/bots/api/#message) is returned, otherwise *True* is returned.
 */
@Serializable
public data class EditMessageLiveLocation(
    /**
     * Required if *inline_message_id* is not specified. Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId? = null,
    /**
     * Required if *inline_message_id* is not specified. Identifier of the message to edit
     */
    public val message_id: Int? = null,
    /**
     * Required if *chat_id* and *message_id* are not specified. Identifier of the inline message
     */
    public val inline_message_id: String? = null,
    /**
     * Latitude of new location
     */
    public val latitude: Double,
    /**
     * Longitude of new location
     */
    public val longitude: Double,
    /**
     * New period in seconds during which the location can be updated, starting from the message send date. If 0x7FFFFFFF is specified, then the location can be updated forever. Otherwise, the new value must not exceed the current *live_period* by more than a day, and the live location expiration date must remain within the next 90 days. If not specified, then *live_period* remains unchanged
     */
    public val live_period: Int? = null,
    /**
     * The radius of uncertainty for the location, measured in meters; 0-1500
     */
    public val horizontal_accuracy: Double? = null,
    /**
     * Direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
     */
    public val heading: Int? = null,
    /**
     * The maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
     */
    public val proximity_alert_radius: Int? = null,
    /**
     * A JSON-serialized object for a new [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards).
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
) : Method<EditMessageLiveLocation, MessageOrTrue> by Companion {
    public companion object : Method<EditMessageLiveLocation, MessageOrTrue> {
        override val _deserializer: KSerializer<Response<MessageOrTrue>> =
                Response.serializer(MessageOrTrue.serializer())

        override val _serializer: KSerializer<EditMessageLiveLocation> = serializer()

        override val _name: String = "editMessageLiveLocation"
    }
}
