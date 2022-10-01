package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * This object represents a chat photo.
 */
@Serializable
public data class ChatPhoto(
  /**
   * File identifier of small (160x160) chat photo. This file_id can be used only for photo download and only for as long as the photo is not changed.
   */
  public val small_file_id: String,
  /**
   * Unique file identifier of small (160x160) chat photo, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
   */
  public val small_file_unique_id: String,
  /**
   * File identifier of big (640x640) chat photo. This file_id can be used only for photo download and only for as long as the photo is not changed.
   */
  public val big_file_id: String,
  /**
   * Unique file identifier of big (640x640) chat photo, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
   */
  public val big_file_unique_id: String,
)
