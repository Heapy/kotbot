package io.heapy.kotbot.bot.model

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Describes an inline message sent by a [Web App](https://core.telegram.org/bots/webapps) on behalf of a user.
 */
@Serializable
public data class SentWebAppMessage(
  /**
   * *Optional*. Identifier of the sent inline message. Available only if there is an [inline keyboard](https://core.telegram.org/bots/api/#inlinekeyboardmarkup) attached to the message.
   */
  public val inline_message_id: String? = null,
)
