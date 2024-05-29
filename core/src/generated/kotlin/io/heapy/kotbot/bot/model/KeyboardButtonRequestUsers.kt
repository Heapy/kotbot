package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object defines the criteria used to request suitable users. Information about the selected users will be shared with the bot when the corresponding button is pressed. [More about requesting users &raquo;](https://core.telegram.org/bots/features#chat-and-user-selection)
 */
@Serializable
public data class KeyboardButtonRequestUsers(
    /**
     * Signed 32-bit identifier of the request that will be received back in the [UsersShared](https://core.telegram.org/bots/api/#usersshared) object. Must be unique within the message
     */
    public val request_id: Int,
    /**
     * *Optional*. Pass *True* to request bots, pass *False* to request regular users. If not specified, no additional restrictions are applied.
     */
    public val user_is_bot: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request premium users, pass *False* to request non-premium users. If not specified, no additional restrictions are applied.
     */
    public val user_is_premium: Boolean? = null,
    /**
     * *Optional*. The maximum number of users to be selected; 1-10. Defaults to 1.
     */
    public val max_quantity: Int? = 1,
    /**
     * *Optional*. Pass *True* to request the users' first and last names
     */
    public val request_name: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request the users' usernames
     */
    public val request_username: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request the users' photos
     */
    public val request_photo: Boolean? = null,
)
