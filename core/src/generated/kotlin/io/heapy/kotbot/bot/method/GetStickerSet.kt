package io.heapy.kotbot.bot.method

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Use this method to get a sticker set. On success, a [StickerSet](https://core.telegram.org/bots/api/#stickerset) object is returned.
 */
@Serializable
public data class GetStickerSet(
  /**
   * Name of the sticker set
   */
  public val name: String,
)
