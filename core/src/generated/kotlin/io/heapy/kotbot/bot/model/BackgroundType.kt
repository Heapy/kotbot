package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.BackgroundTypeSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes the type of a background. Currently, it can be one of
 *
 * * [BackgroundTypeFill](https://core.telegram.org/bots/api/#backgroundtypefill)
 * * [BackgroundTypeWallpaper](https://core.telegram.org/bots/api/#backgroundtypewallpaper)
 * * [BackgroundTypePattern](https://core.telegram.org/bots/api/#backgroundtypepattern)
 * * [BackgroundTypeChatTheme](https://core.telegram.org/bots/api/#backgroundtypechattheme)
 */
@Serializable(with = BackgroundTypeSerializer::class)
public sealed interface BackgroundType
