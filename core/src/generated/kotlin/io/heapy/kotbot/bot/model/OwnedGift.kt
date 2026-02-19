package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.OwnedGiftSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes a gift received and owned by a user or a chat. Currently, it can be one of
 *
 * * [OwnedGiftRegular](https://core.telegram.org/bots/api/#ownedgiftregular)
 * * [OwnedGiftUnique](https://core.telegram.org/bots/api/#ownedgiftunique)
 */
@Serializable(with = OwnedGiftSerializer::class)
public sealed interface OwnedGift
