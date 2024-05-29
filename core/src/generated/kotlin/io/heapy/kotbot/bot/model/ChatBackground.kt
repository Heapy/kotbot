package io.heapy.kotbot.bot.model

import kotlinx.serialization.Serializable

/**
 * This object represents a chat background.
 */
@Serializable
public data class ChatBackground(
    /**
     * Type of the background
     */
    public val type: BackgroundType,
)
