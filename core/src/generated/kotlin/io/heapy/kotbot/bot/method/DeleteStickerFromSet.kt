package io.heapy.kotbot.bot.method

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Use this method to delete a sticker from a set created by the bot. Returns *True* on success.
 */
@Serializable
public data class DeleteStickerFromSet(
  /**
   * File identifier of the sticker
   */
  public val sticker: String,
)
