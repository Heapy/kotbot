package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents the [scope](https://core.telegram.org/bots/api/#botcommandscope) of bot commands, covering all private chats.
 */
@Serializable
public data class BotCommandScopeAllPrivateChats(
    /**
     * Scope type, must be *all_private_chats*
     */
    public val type: String,
) : BotCommandScope
