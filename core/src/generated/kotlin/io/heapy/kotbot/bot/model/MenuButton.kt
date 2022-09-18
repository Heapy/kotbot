package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.MenuButtonSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes the bot's menu button in a private chat. It should be one of
 *
 * * [MenuButtonCommands](https://core.telegram.org/bots/api/#menubuttoncommands)
 * * [MenuButtonWebApp](https://core.telegram.org/bots/api/#menubuttonwebapp)
 * * [MenuButtonDefault](https://core.telegram.org/bots/api/#menubuttondefault)
 */
@Serializable(with = MenuButtonSerializer::class)
public sealed interface MenuButton
