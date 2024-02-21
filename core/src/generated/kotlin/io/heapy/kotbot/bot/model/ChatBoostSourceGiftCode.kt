package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * The boost was obtained by the creation of Telegram Premium gift codes to boost a chat. Each such code boosts the chat 4 times for the duration of the corresponding Telegram Premium subscription.
 */
@Serializable
public data class ChatBoostSourceGiftCode(
    /**
     * Source of the boost, always "gift_code"
     */
    public val source: String = "gift_code",
    /**
     * User for which the gift code was created
     */
    public val user: User,
) : ChatBoostSource
