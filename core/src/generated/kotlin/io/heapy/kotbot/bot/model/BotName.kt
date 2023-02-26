package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents the bot's name.
 */
@Serializable
public data class BotName(
    /**
     * The bot's name
     */
    public val name: String,
)
