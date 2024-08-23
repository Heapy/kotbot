package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object contains information about the quoted part of a message that is replied to by the given message.
 */
@Serializable
public data class TextQuote(
    /**
     * Text of the quoted part of a message that is replied to by the given message
     */
    public val text: String,
    /**
     * *Optional*. Special entities that appear in the quote. Currently, only *bold*, *italic*, *underline*, *strikethrough*, *spoiler*, and *custom_emoji* entities are kept in quotes.
     */
    public val entities: List<MessageEntity>? = null,
    /**
     * Approximate quote position in the original message in UTF-16 code units as specified by the sender
     */
    public val position: Int,
    /**
     * *Optional*. True, if the quote was chosen manually by the message sender. Otherwise, the quote was added automatically by the server.
     */
    public val is_manual: Boolean? = null,
)
