package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object defines the criteria used to request a suitable user. The identifier of the selected user will be shared with the bot when the corresponding button is pressed. [More about requesting users &raquo;](https://core.telegram.org/bots/features#chat-and-user-selection)
 */
@Serializable
public data class KeyboardButtonRequestUser(
    /**
     * Signed 32-bit identifier of the request, which will be received back in the [UserShared](https://core.telegram.org/bots/api/#usershared) object. Must be unique within the message
     */
    public val request_id: Int,
    /**
     * *Optional*. Pass *True* to request a bot, pass *False* to request a regular user. If not specified, no additional restrictions are applied.
     */
    public val user_is_bot: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request a premium user, pass *False* to request a non-premium user. If not specified, no additional restrictions are applied.
     */
    public val user_is_premium: Boolean? = null,
)
