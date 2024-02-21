package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.ChatBoostSourceSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes the source of a chat boost. It can be one of
 *
 * * [ChatBoostSourcePremium](https://core.telegram.org/bots/api/#chatboostsourcepremium)
 * * [ChatBoostSourceGiftCode](https://core.telegram.org/bots/api/#chatboostsourcegiftcode)
 * * [ChatBoostSourceGiveaway](https://core.telegram.org/bots/api/#chatboostsourcegiveaway)
 */
@Serializable(with = ChatBoostSourceSerializer::class)
public sealed interface ChatBoostSource
