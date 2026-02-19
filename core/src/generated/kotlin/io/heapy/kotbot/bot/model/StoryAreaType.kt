package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.StoryAreaTypeSerializer
import kotlinx.serialization.Serializable

/**
 * Describes the type of a clickable area on a story. Currently, it can be one of
 *
 * * [StoryAreaTypeLocation](https://core.telegram.org/bots/api/#storyareatypelocation)
 * * [StoryAreaTypeSuggestedReaction](https://core.telegram.org/bots/api/#storyareatypesuggestedreaction)
 * * [StoryAreaTypeLink](https://core.telegram.org/bots/api/#storyareatypelink)
 * * [StoryAreaTypeWeather](https://core.telegram.org/bots/api/#storyareatypeweather)
 * * [StoryAreaTypeUniqueGift](https://core.telegram.org/bots/api/#storyareatypeuniquegift)
 */
@Serializable(with = StoryAreaTypeSerializer::class)
public sealed interface StoryAreaType
