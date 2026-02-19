package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.InputStoryContentSerializer
import kotlinx.serialization.Serializable

/**
 * This object describes the content of a story to post. Currently, it can be one of
 *
 * * [InputStoryContentPhoto](https://core.telegram.org/bots/api/#inputstorycontentphoto)
 * * [InputStoryContentVideo](https://core.telegram.org/bots/api/#inputstorycontentvideo)
 */
@Serializable(with = InputStoryContentSerializer::class)
public sealed interface InputStoryContent
