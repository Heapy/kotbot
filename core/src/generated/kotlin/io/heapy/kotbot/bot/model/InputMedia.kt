package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.InputMediaSerializer
import kotlinx.serialization.Serializable

/**
 * This object represents the content of a media message to be sent. It should be one of
 *
 * * [InputMediaAnimation](https://core.telegram.org/bots/api/#inputmediaanimation)
 * * [InputMediaDocument](https://core.telegram.org/bots/api/#inputmediadocument)
 * * [InputMediaAudio](https://core.telegram.org/bots/api/#inputmediaaudio)
 * * [InputMediaPhoto](https://core.telegram.org/bots/api/#inputmediaphoto)
 * * [InputMediaVideo](https://core.telegram.org/bots/api/#inputmediavideo)
 */
@Serializable(with = InputMediaSerializer::class)
public sealed interface InputMedia
