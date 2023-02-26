package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents an inline button that switches the current user to inline mode in a chosen chat, with an optional default inline query.
 */
@Serializable
public data class SwitchInlineQueryChosenChat(
    /**
     * *Optional*. The default inline query to be inserted in the input field. If left empty, only the bot's username will be inserted
     */
    public val query: String? = null,
    /**
     * *Optional*. True, if private chats with users can be chosen
     */
    public val allow_user_chats: Boolean? = null,
    /**
     * *Optional*. True, if private chats with bots can be chosen
     */
    public val allow_bot_chats: Boolean? = null,
    /**
     * *Optional*. True, if group and supergroup chats can be chosen
     */
    public val allow_group_chats: Boolean? = null,
    /**
     * *Optional*. True, if channel chats can be chosen
     */
    public val allow_channel_chats: Boolean? = null,
)
