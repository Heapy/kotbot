package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents the bot's short description.
 */
@Serializable
public data class BotShortDescription(
    /**
     * The bot's short description
     */
    public val short_description: String,
)
