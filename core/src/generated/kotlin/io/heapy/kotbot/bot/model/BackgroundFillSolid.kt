package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The background is filled using the selected color.
 */
@Serializable
public data class BackgroundFillSolid(
    /**
     * Type of the background fill, always "solid"
     */
    public val type: String = "solid",
    /**
     * The color of the background fill in the RGB24 format
     */
    public val color: Int,
) : BackgroundFill
