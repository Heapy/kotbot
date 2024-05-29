package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * This object defines the criteria used to request a suitable chat. Information about the selected chat will be shared with the bot when the corresponding button is pressed. The bot will be granted requested rights in the chat if appropriate. [More about requesting chats &raquo;](https://core.telegram.org/bots/features#chat-and-user-selection).
 */
@Serializable
public data class KeyboardButtonRequestChat(
    /**
     * Signed 32-bit identifier of the request, which will be received back in the [ChatShared](https://core.telegram.org/bots/api/#chatshared) object. Must be unique within the message
     */
    public val request_id: Int,
    /**
     * Pass *True* to request a channel chat, pass *False* to request a group or a supergroup chat.
     */
    public val chat_is_channel: Boolean,
    /**
     * *Optional*. Pass *True* to request a forum supergroup, pass *False* to request a non-forum chat. If not specified, no additional restrictions are applied.
     */
    public val chat_is_forum: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request a supergroup or a channel with a username, pass *False* to request a chat without a username. If not specified, no additional restrictions are applied.
     */
    public val chat_has_username: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request a chat owned by the user. Otherwise, no additional restrictions are applied.
     */
    public val chat_is_created: Boolean? = null,
    /**
     * *Optional*. A JSON-serialized object listing the required administrator rights of the user in the chat. The rights must be a superset of *bot_administrator_rights*. If not specified, no additional restrictions are applied.
     */
    public val user_administrator_rights: ChatAdministratorRights? = null,
    /**
     * *Optional*. A JSON-serialized object listing the required administrator rights of the bot in the chat. The rights must be a subset of *user_administrator_rights*. If not specified, no additional restrictions are applied.
     */
    public val bot_administrator_rights: ChatAdministratorRights? = null,
    /**
     * *Optional*. Pass *True* to request a chat with the bot as a member. Otherwise, no additional restrictions are applied.
     */
    public val bot_is_member: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request the chat's title
     */
    public val request_title: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request the chat's username
     */
    public val request_username: Boolean? = null,
    /**
     * *Optional*. Pass *True* to request the chat's photo
     */
    public val request_photo: Boolean? = null,
)
