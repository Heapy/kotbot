package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.InputPollOptionMediaSerializer
import kotlinx.serialization.Serializable

/**
 * This object represents the content of a poll option to be sent. It should be one of
 *
 * * [InputMediaAnimation](https://core.telegram.org/bots/api/#inputmediaanimation)
 * * [InputMediaLink](https://core.telegram.org/bots/api/#inputmedialink)
 * * [InputMediaLivePhoto](https://core.telegram.org/bots/api/#inputmedialivephoto)
 * * [InputMediaLocation](https://core.telegram.org/bots/api/#inputmedialocation)
 * * [InputMediaPhoto](https://core.telegram.org/bots/api/#inputmediaphoto)
 * * [InputMediaSticker](https://core.telegram.org/bots/api/#inputmediasticker)
 * * [InputMediaVenue](https://core.telegram.org/bots/api/#inputmediavenue)
 * * [InputMediaVideo](https://core.telegram.org/bots/api/#inputmediavideo)
 */
@Serializable(with = InputPollOptionMediaSerializer::class)
public sealed interface InputPollOptionMedia
