package io.heapy.kotbot.bot.model

import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a location on a map. By default, the location will be sent by the user. Alternatively, you can use *input_message_content* to send a message with the specified content instead of the location.
 */
@Serializable
public data class InlineQueryResultLocation(
    /**
     * Type of the result, must be *location*
     */
    public val type: String = "location",
    /**
     * Unique identifier for this result, 1-64 Bytes
     */
    public val id: String,
    /**
     * Location latitude in degrees
     */
    public val latitude: Double,
    /**
     * Location longitude in degrees
     */
    public val longitude: Double,
    /**
     * Location title
     */
    public val title: String,
    /**
     * *Optional*. The radius of uncertainty for the location, measured in meters; 0-1500
     */
    public val horizontal_accuracy: Double? = null,
    /**
     * *Optional*. Period in seconds during which the location can be updated, should be between 60 and 86400, or 0x7FFFFFFF for live locations that can be edited indefinitely.
     */
    public val live_period: Int? = null,
    /**
     * *Optional*. For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
     */
    public val heading: Int? = null,
    /**
     * *Optional*. For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
     */
    public val proximity_alert_radius: Int? = null,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the location
     */
    public val input_message_content: InputMessageContent? = null,
    /**
     * *Optional*. Url of the thumbnail for the result
     */
    public val thumbnail_url: String? = null,
    /**
     * *Optional*. Thumbnail width
     */
    public val thumbnail_width: Int? = null,
    /**
     * *Optional*. Thumbnail height
     */
    public val thumbnail_height: Int? = null,
) : InlineQueryResult
