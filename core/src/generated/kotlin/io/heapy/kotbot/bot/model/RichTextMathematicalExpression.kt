package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A mathematical expression.
 */
@Serializable
public data class RichTextMathematicalExpression(
    /**
     * Type of the rich text, always "mathematical_expression"
     */
    public val type: String,
    /**
     * The expression in LaTeX format
     */
    public val expression: String,
) : RichText
