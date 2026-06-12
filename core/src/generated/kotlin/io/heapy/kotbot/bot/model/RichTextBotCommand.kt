package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A bot command.
 */
@Serializable
public data class RichTextBotCommand(
    /**
     * Type of the rich text, always "bot_command"
     */
    public val type: String,
    /**
     * The text
     */
    public val text: RichText,
    /**
     * The bot command
     */
    public val bot_command: String,
) : RichText
