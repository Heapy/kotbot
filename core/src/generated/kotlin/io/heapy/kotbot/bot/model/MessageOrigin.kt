package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.MessageOriginSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes the origin of a message. It can be one of
 *
 * * [MessageOriginUser](https://core.telegram.org/bots/api/#messageoriginuser)
 * * [MessageOriginHiddenUser](https://core.telegram.org/bots/api/#messageoriginhiddenuser)
 * * [MessageOriginChat](https://core.telegram.org/bots/api/#messageoriginchat)
 * * [MessageOriginChannel](https://core.telegram.org/bots/api/#messageoriginchannel)
 */
@Serializable(with = MessageOriginSerializer::class)
public sealed interface MessageOrigin
