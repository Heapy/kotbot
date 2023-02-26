package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents the bot's description.
 */
@Serializable
public data class BotDescription(
    /**
     * The bot's description
     */
    public val description: String,
)
