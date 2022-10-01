package io.heapy.kotbot.bot.method

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Use this method to move a sticker in a set created by the bot to a specific position. Returns *True* on success.
 */
@Serializable
public data class SetStickerPositionInSet(
  /**
   * File identifier of the sticker
   */
  public val sticker: String,
  /**
   * New sticker position in the set, zero-based
   */
  public val position: Int,
)
