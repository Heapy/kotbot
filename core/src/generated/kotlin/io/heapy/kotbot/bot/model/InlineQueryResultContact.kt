package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a contact with a phone number. By default, this contact will be sent by the user. Alternatively, you can use *input_message_content* to send a message with the specified content instead of the contact.
 */
@Serializable
public data class InlineQueryResultContact(
    /**
     * Type of the result, must be *contact*
     */
    public val type: String,
    /**
     * Unique identifier for this result, 1-64 Bytes
     */
    public val id: String,
    /**
     * Contact's phone number
     */
    public val phone_number: String,
    /**
     * Contact's first name
     */
    public val first_name: String,
    /**
     * *Optional*. Contact's last name
     */
    public val last_name: String? = null,
    /**
     * *Optional*. Additional data about the contact in the form of a [vCard](https://en.wikipedia.org/wiki/VCard), 0-2048 bytes
     */
    public val vcard: String? = null,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
    /**
     * *Optional*. Content of the message to be sent instead of the contact
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
