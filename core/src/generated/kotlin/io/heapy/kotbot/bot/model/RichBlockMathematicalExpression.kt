package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A block with a mathematical expression in LaTeX format, corresponding to the custom HTML tag `<tg-math-block>`.
 */
@Serializable
public data class RichBlockMathematicalExpression(
    /**
     * Type of the block, always "mathematical_expression"
     */
    public val type: String,
    /**
     * The mathematical expression in LaTeX format
     */
    public val expression: String,
) : RichBlock
