package io.heapy.kotbot.bot.model

import kotlin.Int
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * This object represent a user's profile pictures.
 */
@Serializable
public data class UserProfilePhotos(
  /**
   * Total number of profile pictures the target user has
   */
  public val total_count: Int,
  /**
   * Requested profile pictures (in up to 4 sizes each)
   */
  public val photos: List<List<PhotoSize>>,
)
