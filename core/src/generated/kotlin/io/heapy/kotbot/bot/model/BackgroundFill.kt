package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.BackgroundFillSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes the way a background is filled based on the selected colors. Currently, it can be one of
 *
 * * [BackgroundFillSolid](https://core.telegram.org/bots/api/#backgroundfillsolid)
 * * [BackgroundFillGradient](https://core.telegram.org/bots/api/#backgroundfillgradient)
 * * [BackgroundFillFreeformGradient](https://core.telegram.org/bots/api/#backgroundfillfreeformgradient)
 */
@Serializable(with = BackgroundFillSerializer::class)
public sealed interface BackgroundFill
