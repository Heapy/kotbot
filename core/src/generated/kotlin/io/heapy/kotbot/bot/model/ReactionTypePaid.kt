package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The reaction is paid.
 */
@Serializable
public data class ReactionTypePaid(
    /**
     * Type of the reaction, always "paid"
     */
    public val type: String,
) : ReactionType
