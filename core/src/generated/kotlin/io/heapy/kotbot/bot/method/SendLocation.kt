package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.Method
import io.heapy.kotbot.bot.Response
import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.Message
import io.heapy.kotbot.bot.model.ReplyMarkup
import io.heapy.kotbot.bot.model.ReplyParameters
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Use this method to send point on the map. On success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned.
 */
@Serializable
public data class SendLocation(
    /**
     * Unique identifier of the business connection on behalf of which the message will be sent
     */
    public val business_connection_id: String? = null,
    /**
     * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
     */
    public val chat_id: ChatId,
    /**
     * Unique identifier for the target message thread (topic) of the forum; for forum supergroups only
     */
    public val message_thread_id: Int? = null,
    /**
     * Latitude of the location
     */
    public val latitude: Double,
    /**
     * Longitude of the location
     */
    public val longitude: Double,
    /**
     * The radius of uncertainty for the location, measured in meters; 0-1500
     */
    public val horizontal_accuracy: Double? = null,
    /**
     * Period in seconds during which the location will be updated (see [Live Locations](https://telegram.org/blog/live-locations), should be between 60 and 86400, or 0x7FFFFFFF for live locations that can be edited indefinitely.
     */
    public val live_period: Int? = null,
    /**
     * For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
     */
    public val heading: Int? = null,
    /**
     * For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
     */
    public val proximity_alert_radius: Int? = null,
    /**
     * Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
     */
    public val disable_notification: Boolean? = null,
    /**
     * Protects the contents of the sent message from forwarding and saving
     */
    public val protect_content: Boolean? = null,
    /**
     * Unique identifier of the message effect to be added to the message; for private chats only
     */
    public val message_effect_id: String? = null,
    /**
     * Description of the message to reply to
     */
    public val reply_parameters: ReplyParameters? = null,
    /**
     * Additional interface options. A JSON-serialized object for an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards), [custom reply keyboard](https://core.telegram.org/bots/features#keyboards), instructions to remove a reply keyboard or to force a reply from the user
     */
    public val reply_markup: ReplyMarkup? = null,
) : Method<SendLocation, Message> by Companion {
    public companion object : Method<SendLocation, Message> {
        override val _deserializer: KSerializer<Response<Message>> =
                Response.serializer(Message.serializer())

        override val _serializer: KSerializer<SendLocation> = serializer()

        override val _name: String = "sendLocation"
    }
}
