package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.ReactionTypeSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes the type of a reaction. Currently, it can be one of
 *
 * * [ReactionTypeEmoji](https://core.telegram.org/bots/api/#reactiontypeemoji)
 * * [ReactionTypeCustomEmoji](https://core.telegram.org/bots/api/#reactiontypecustomemoji)
 */
@Serializable(with = ReactionTypeSerializer::class)
public sealed interface ReactionType
