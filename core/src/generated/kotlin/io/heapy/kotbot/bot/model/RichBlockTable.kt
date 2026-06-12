package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * A table, corresponding to the HTML tag `<table>`.
 */
@Serializable
public data class RichBlockTable(
    /**
     * Type of the block, always "table"
     */
    public val type: String,
    /**
     * Cells of the table
     */
    public val cells: List<List<RichBlockTableCell>>,
    /**
     * *Optional*. *True*, if the table has borders
     */
    public val is_bordered: Boolean? = null,
    /**
     * *Optional*. *True*, if the table is striped
     */
    public val is_striped: Boolean? = null,
    /**
     * *Optional*. Caption of the table
     */
    public val caption: RichText? = null,
) : RichBlock
