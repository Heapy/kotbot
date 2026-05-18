package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.InputPollMediaSerializer
import kotlinx.serialization.Serializable

/**
 * This object represents the content of a poll description or a quiz explanation to be sent. It should be one of
 *
 * * [InputMediaAnimation](https://core.telegram.org/bots/api/#inputmediaanimation)
 * * [InputMediaAudio](https://core.telegram.org/bots/api/#inputmediaaudio)
 * * [InputMediaDocument](https://core.telegram.org/bots/api/#inputmediadocument)
 * * [InputMediaLivePhoto](https://core.telegram.org/bots/api/#inputmedialivephoto)
 * * [InputMediaLocation](https://core.telegram.org/bots/api/#inputmedialocation)
 * * [InputMediaPhoto](https://core.telegram.org/bots/api/#inputmediaphoto)
 * * [InputMediaVenue](https://core.telegram.org/bots/api/#inputmediavenue)
 * * [InputMediaVideo](https://core.telegram.org/bots/api/#inputmediavideo)
 */
@Serializable(with = InputPollMediaSerializer::class)
public sealed interface InputPollMedia
