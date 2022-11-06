package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a [result](https://core.telegram.org/bots/api/#inlinequeryresult) of an inline query that was chosen by the user and sent to their chat partner.
 */
@Serializable
public data class ChosenInlineResult(
    /**
     * The unique identifier for the result that was chosen
     */
    public val result_id: String,
    /**
     * The user that chose the result
     */
    public val from: User,
    /**
     * *Optional*. Sender location, only for bots that require user location
     */
    public val location: Location? = null,
    /**
     * *Optional*. Identifier of the sent inline message. Available only if there is an [inline keyboard](https://core.telegram.org/bots/api/#inlinekeyboardmarkup) attached to the message. Will be also received in [callback queries](https://core.telegram.org/bots/api/#callbackquery) and can be used to [edit](https://core.telegram.org/bots/api/#updating-messages) the message.
     */
    public val inline_message_id: String? = null,
    /**
     * The query that was used to obtain the result
     */
    public val query: String,
)
