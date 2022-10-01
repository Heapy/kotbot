package io.heapy.kotbot.bot.method

import kotlin.Boolean
import kotlinx.serialization.Serializable

/**
 * Use this method to remove webhook integration if you decide to switch back to [getUpdates](https://core.telegram.org/bots/api/#getupdates). Returns *True* on success.
 */
@Serializable
public data class DeleteWebhook(
  /**
   * Pass *True* to drop all pending updates
   */
  public val drop_pending_updates: Boolean? = null,
)
