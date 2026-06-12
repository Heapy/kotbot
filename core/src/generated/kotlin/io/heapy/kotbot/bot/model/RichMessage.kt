package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Rich formatted message.
 */
@Serializable
public data class RichMessage(
    /**
     * Content of the message
     */
    public val blocks: List<RichBlock>,
    /**
     * *Optional*. *True*, if the rich message must be shown right-to-left
     */
    public val is_rtl: Boolean? = null,
)
