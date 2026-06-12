package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A block with a voice note, corresponding to the HTML tag `<audio>`.
 */
@Serializable
public data class RichBlockVoiceNote(
    /**
     * Type of the block, always "voice_note"
     */
    public val type: String,
    /**
     * The voice note
     */
    public val voice_note: Voice,
    /**
     * *Optional*. Caption of the block
     */
    public val caption: RichBlockCaption? = null,
) : RichBlock
