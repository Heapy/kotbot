package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A preformatted text block, corresponding to the nested HTML tags `<pre>` and `<code>`.
 */
@Serializable
public data class RichBlockPreformatted(
    /**
     * Type of the block, always "pre"
     */
    public val type: String,
    /**
     * Text of the block
     */
    public val text: RichText,
    /**
     * *Optional*. The programming language of the text
     */
    public val language: String? = null,
) : RichBlock
