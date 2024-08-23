package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The background is taken directly from a built-in chat theme.
 */
@Serializable
public data class BackgroundTypeChatTheme(
    /**
     * Type of the background, always "chat_theme"
     */
    public val type: String,
    /**
     * Name of the chat theme, which is usually an emoji
     */
    public val theme_name: String,
) : BackgroundType
