package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.MaybeInaccessibleMessageSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes a message that can be inaccessible to the bot. It can be one of
 *
 * * [Message](https://core.telegram.org/bots/api/#message)
 * * [InaccessibleMessage](https://core.telegram.org/bots/api/#inaccessiblemessage)
 */
@Serializable(with = MaybeInaccessibleMessageSerializer::class)
public sealed interface MaybeInaccessibleMessage
