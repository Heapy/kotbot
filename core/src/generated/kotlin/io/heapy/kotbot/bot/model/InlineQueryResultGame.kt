package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents a [Game](https://core.telegram.org/bots/api/#games).
 */
@Serializable
public data class InlineQueryResultGame(
    /**
     * Type of the result, must be *game*
     */
    public val type: String = "game",
    /**
     * Unique identifier for this result, 1-64 bytes
     */
    public val id: String,
    /**
     * Short name of the game
     */
    public val game_short_name: String,
    /**
     * *Optional*. [Inline keyboard](https://core.telegram.org/bots/features#inline-keyboards) attached to the message
     */
    public val reply_markup: InlineKeyboardMarkup? = null,
) : InlineQueryResult
