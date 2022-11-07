package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents type of a poll, which is allowed to be created and sent when the corresponding button is pressed.
 */
@Serializable
public data class KeyboardButtonPollType(
    /**
     * *Optional*. If *quiz* is passed, the user will be allowed to create only polls in the quiz mode. If *regular* is passed, only regular polls will be allowed. Otherwise, the user will be allowed to create a poll of any type.
     */
    public val type: String? = null,
)
