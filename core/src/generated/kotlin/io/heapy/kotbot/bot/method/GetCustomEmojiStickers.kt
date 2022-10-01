package io.heapy.kotbot.bot.method

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Use this method to get information about custom emoji stickers by their identifiers. Returns an Array of [Sticker](https://core.telegram.org/bots/api/#sticker) objects.
 */
@Serializable
public data class GetCustomEmojiStickers(
  /**
   * List of custom emoji identifiers. At most 200 custom emoji identifiers can be specified.
   */
  public val custom_emoji_ids: List<String>,
)
