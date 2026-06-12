package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Cell in a table.
 */
@Serializable
public data class RichBlockTableCell(
    /**
     * *Optional*. Text in the cell. If omitted, then the cell is invisible.
     */
    public val text: RichText? = null,
    /**
     * *Optional*. *True*, if the cell is a header cell
     */
    public val is_header: Boolean? = null,
    /**
     * *Optional*. The number of columns the cell spans if it is bigger than 1
     */
    public val colspan: Int? = null,
    /**
     * *Optional*. The number of rows the cell spans if it is bigger than 1
     */
    public val rowspan: Int? = null,
    /**
     * Horizontal cell content alignment. Currently, must be one of "left", "center", or "right".
     */
    public val align: String,
    /**
     * Vertical cell content alignment. Currently, must be one of "top", "middle", or "bottom".
     */
    public val valign: String,
)
