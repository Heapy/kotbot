package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.InputPaidMediaSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes the paid media to be sent. Currently, it can be one of
 *
 * * [InputPaidMediaPhoto](https://core.telegram.org/bots/api/#inputpaidmediaphoto)
 * * [InputPaidMediaVideo](https://core.telegram.org/bots/api/#inputpaidmediavideo)
 */
@Serializable(with = InputPaidMediaSerializer::class)
public sealed interface InputPaidMedia
