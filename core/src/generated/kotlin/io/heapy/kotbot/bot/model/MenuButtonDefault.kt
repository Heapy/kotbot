package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes that no specific value for the menu button was set.
 */
@Serializable
public data class MenuButtonDefault(
    /**
     * Type of the button, must be *default*
     */
    public val type: String,
) : MenuButton
