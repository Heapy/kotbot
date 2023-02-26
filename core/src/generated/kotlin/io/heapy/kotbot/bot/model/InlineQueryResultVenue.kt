package io.heapy.kotbot.bot.model

import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a venue. By default, the venue will be sent by the user. Alternatively, you can use *input_message_content* to send a message with the specified content instead of the venue.
 */
@Serializable
public data class InlineQueryResultVenue(
    /**
     * Type of the result, must be *venue*
     */
    public val type: String = "venue",
    /**
     * Unique identifier for this result, 1-64 Bytes
     */
    public val id: String,
    /**
     * Latitude of the venue location in degrees
     */
    public val latitude: Double,
    /**
     * Longitude of the venue location in degrees
     */
    public val longitude: Double,
    /**
     * Title of the venue
     */
    public val title: String,
    /**
     * Address of the venue
     */
    public val address: String,
    /**
     * *Optional*. Foursquare identifier of the venue if known
     */
    public val foursquare_id: String? = null,
    /**
     * *Optional*. Foursquare type of the venue, if known. (For example, "arts_entertainment/default", "arts_entertainment/aquarium" or "food/icecream".)
     */
    public val foursquare_type: String? = null,
    /**
     * *Optional*. Google Places identifier of the venue
     */
    public val google_place_id: String? = null,
    /**
     * *Optional*. Google Places type of the venue. (See [supported types](https://developers.google.com/places/web-service/supported_types).)
     */
    public val google_place_type: String? = null,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the venue
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
