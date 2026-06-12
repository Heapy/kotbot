package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * An item of a list.
 */
@Serializable
public data class RichBlockListItem(
    /**
     * Label of the item
     */
    public val label: String,
    /**
     * The content of the item
     */
    public val blocks: List<RichBlock>,
    /**
     * *Optional*. *True*, if the item has a checkbox
     */
    public val has_checkbox: Boolean? = null,
    /**
     * *Optional*. *True*, if the item has a checked checkbox
     */
    public val is_checked: Boolean? = null,
    /**
     * *Optional*. For ordered lists, the numeric value of the item label
     */
    public val `value`: Int? = null,
    /**
     * *Optional*. For ordered lists, the type of the item label; must be one of "a" for lowercase letters, "A" for uppercase letters, "i" for lowercase Roman numerals, "I" for uppercase Roman numerals, or "1" for decimal numbers
     */
    public val type: String? = null,
)
