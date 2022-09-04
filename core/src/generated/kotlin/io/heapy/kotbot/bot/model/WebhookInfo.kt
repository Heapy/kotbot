package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Describes the current status of a webhook.
 */
@Serializable
public data class WebhookInfo(
  /**
   * Webhook URL, may be empty if webhook is not set up
   */
  public val url: String,
  /**
   * *True*, if a custom certificate was provided for webhook certificate checks
   */
  public val has_custom_certificate: Boolean,
  /**
   * Number of updates awaiting delivery
   */
  public val pending_update_count: Int,
  /**
   * *Optional*. Currently used webhook IP address
   */
  public val ip_address: String? = null,
  /**
   * *Optional*. Unix time for the most recent error that happened when trying to deliver an update via webhook
   */
  public val last_error_date: Int? = null,
  /**
   * *Optional*. Error message in human-readable format for the most recent error that happened when trying to deliver an update via webhook
   */
  public val last_error_message: String? = null,
  /**
   * *Optional*. Unix time of the most recent error that happened when trying to synchronize available updates with Telegram datacenters
   */
  public val last_synchronization_error_date: Int? = null,
  /**
   * *Optional*. The maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery
   */
  public val max_connections: Int? = null,
  /**
   * *Optional*. A list of update types the bot is subscribed to. Defaults to all update types except *chat\_member*
   */
  public val allowed_updates: List<String>? = null,
)
