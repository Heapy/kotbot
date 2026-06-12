package io.heapy.kotbot.bot.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Formatted date and time.
 */
@Serializable
public data class RichTextDateTime(
    /**
     * Type of the rich text, always "date_time"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The Unix time associated with the entity
     */
    public val unix_time: Long,
    /**
     * The string that defines the formatting of the date and time. See [date-time entity formatting](https://core.telegram.org/bots/api/#date-time-entity-formatting) for more details.
     */
    public val date_time_format: String,
) : RichText
