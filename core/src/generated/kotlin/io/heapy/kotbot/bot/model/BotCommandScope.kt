package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.AnyOfObject
import io.heapy.kotbot.bot.BotCommandScopeSerializer
import kotlinx.serialization.Serializable

/**
 * This object represents the scope to which bot commands are applied. Currently, the following 7 scopes are supported:
 *
 * * [BotCommandScopeDefault](https://core.telegram.org/bots/api/#botcommandscopedefault)
 * * [BotCommandScopeAllPrivateChats](https://core.telegram.org/bots/api/#botcommandscopeallprivatechats)
 * * [BotCommandScopeAllGroupChats](https://core.telegram.org/bots/api/#botcommandscopeallgroupchats)
 * * [BotCommandScopeAllChatAdministrators](https://core.telegram.org/bots/api/#botcommandscopeallchatadministrators)
 * * [BotCommandScopeChat](https://core.telegram.org/bots/api/#botcommandscopechat)
 * * [BotCommandScopeChatAdministrators](https://core.telegram.org/bots/api/#botcommandscopechatadministrators)
 * * [BotCommandScopeChatMember](https://core.telegram.org/bots/api/#botcommandscopechatmember)
 */
@Serializable(with = BotCommandScopeSerializer::class)
@AnyOfObject
public sealed interface BotCommandScope
