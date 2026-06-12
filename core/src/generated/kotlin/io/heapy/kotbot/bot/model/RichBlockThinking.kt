package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * A block with a "Thinking…" placeholder, corresponding to the custom HTML tag `<tg-thinking>`. The block may be used only in [sendRichMessageDraft](https://core.telegram.org/bots/api/#sendrichmessagedraft), therefore it can't be received in messages. See [https://t.me/addemoji/AIActions](https://t.me/addemoji/AIActions) for examples of custom emoji, which are recommended for usage in the block.
 */
@Serializable
public data class RichBlockThinking(
    /**
     * Type of the block, always "thinking"
     */
    public val type: String,
    /**
     * Text of the block. See [https://t.me/addemoji/AIActions](https://t.me/addemoji/AIActions) for examples of custom emoji, which are recommended for usage in the block.
     */
    public val text: RichText,
) : RichBlock
