package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents an incoming callback query from a callback button in an [inline keyboard](https://core.telegram.org/bots/features#inline-keyboards). If the button that originated the query was attached to a message sent by the bot, the field *message* will be present. If the button was attached to a message sent via the bot (in [inline mode](https://core.telegram.org/bots/api/#inline-mode)), the field *inline_message_id* will be present. Exactly one of the fields *data* or *game_short_name* will be present.
 */
@Serializable
public data class CallbackQuery(
    /**
     * Unique identifier for this query
     */
    public val id: String,
    /**
     * Sender
     */
    public val from: User,
    /**
     * *Optional*. Message with the callback button that originated the query. Note that message content and message date will not be available if the message is too old
     */
    public val message: Message? = null,
    /**
     * *Optional*. Identifier of the message sent via the bot in inline mode, that originated the query.
     */
    public val inline_message_id: String? = null,
    /**
     * Global identifier, uniquely corresponding to the chat to which the message with the callback button was sent. Useful for high scores in [games](https://core.telegram.org/bots/api/#games).
     */
    public val chat_instance: String,
    /**
     * *Optional*. Data associated with the callback button. Be aware that the message originated the query can contain no callback buttons with this data.
     */
    public val `data`: String? = null,
    /**
     * *Optional*. Short name of a [Game](https://core.telegram.org/bots/api/#games) to be returned, serves as the unique identifier for the game
     */
    public val game_short_name: String? = null,
)
