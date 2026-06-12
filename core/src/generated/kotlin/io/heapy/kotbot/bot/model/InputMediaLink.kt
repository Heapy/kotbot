package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents an HTTP link to be sent.
 */
@Serializable
public data class InputMediaLink(
    /**
     * Type of the result, must be *link*
     */
    public val type: String,
    /**
     * HTTP URL of the link
     */
    public val url: String,
) : InputPollOptionMedia
