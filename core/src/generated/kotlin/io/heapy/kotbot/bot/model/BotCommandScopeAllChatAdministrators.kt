package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents the [scope](https://core.telegram.org/bots/api/#botcommandscope) of bot commands, covering all group and supergroup chat administrators.
 */
@Serializable
public data class BotCommandScopeAllChatAdministrators(
    /**
     * Scope type, must be *all_chat_administrators*
     */
    public val type: String,
) : BotCommandScope
