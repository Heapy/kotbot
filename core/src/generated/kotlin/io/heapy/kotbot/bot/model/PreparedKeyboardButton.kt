package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes a keyboard button to be used by a user of a Mini App.
 */
@Serializable
public data class PreparedKeyboardButton(
    /**
     * Unique identifier of the keyboard button
     */
    public val id: String,
)
