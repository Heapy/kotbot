package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.PaidMediaSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes paid media. Currently, it can be one of
 *
 * * [PaidMediaPreview](https://core.telegram.org/bots/api/#paidmediapreview)
 * * [PaidMediaPhoto](https://core.telegram.org/bots/api/#paidmediaphoto)
 * * [PaidMediaVideo](https://core.telegram.org/bots/api/#paidmediavideo)
 */
@Serializable(with = PaidMediaSerializer::class)
public sealed interface PaidMedia
