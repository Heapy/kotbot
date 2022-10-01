package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents one size of a photo or a [file](https://core.telegram.org/bots/api/#document) / [sticker](https://core.telegram.org/bots/api/#sticker) thumbnail.
 */
@Serializable
public data class PhotoSize(
  /**
   * Identifier for this file, which can be used to download or reuse the file
   */
  public val file_id: String,
  /**
   * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
   */
  public val file_unique_id: String,
  /**
   * Photo width
   */
  public val width: Int,
  /**
   * Photo height
   */
  public val height: Int,
  /**
   * *Optional*. File size in bytes
   */
  public val file_size: Long? = null,
)
