package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A footer, corresponding to the HTML tag `<footer>`.
 */
@Serializable
public data class RichBlockFooter(
    /**
     * Type of the block, always "footer"
     */
    public val type: String,
    /**
     * Text of the block
     */
    public val text: RichText,
) : RichBlock
