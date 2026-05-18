package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.PaidMediaSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes paid media. Currently, it can be one of
 *
 * * [PaidMediaLivePhoto](https://core.telegram.org/bots/api/#paidmedialivephoto)
 * * [PaidMediaPhoto](https://core.telegram.org/bots/api/#paidmediaphoto)
 * * [PaidMediaPreview](https://core.telegram.org/bots/api/#paidmediapreview)
 * * [PaidMediaVideo](https://core.telegram.org/bots/api/#paidmediavideo)
 */
@Serializable(with = PaidMediaSerializer::class)
public sealed interface PaidMedia
