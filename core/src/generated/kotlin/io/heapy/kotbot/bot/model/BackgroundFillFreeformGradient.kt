package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * The background is a freeform gradient that rotates after every message in the chat.
 */
@Serializable
public data class BackgroundFillFreeformGradient(
    /**
     * Type of the background fill, always "freeform_gradient"
     */
    public val type: String = "freeform_gradient",
    /**
     * A list of the 3 or 4 base colors that are used to generate the freeform gradient in the RGB24 format
     */
    public val colors: List<Int>,
) : BackgroundFill
