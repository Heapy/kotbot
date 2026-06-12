package io.heapy.kotbot.bot.model

import kotlinx.serialization.Serializable

/**
 * Represents the [content](https://core.telegram.org/bots/api/#inputmessagecontent) of a rich message to be sent as the result of an inline query.
 */
@Serializable
public data class InputRichMessageContent(
    /**
     * The message to be sent
     */
    public val rich_message: InputRichMessage,
) : InputMessageContent
