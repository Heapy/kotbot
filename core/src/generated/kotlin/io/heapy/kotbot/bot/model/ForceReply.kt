package io.heapy.kotbot.bot.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Upon receiving a message with this object, Telegram clients will display a reply interface to the user (act as if the user has selected the bot's message and tapped 'Reply'). This can be extremely useful if you want to create user-friendly step-by-step interfaces without having to sacrifice [privacy mode](https://core.telegram.org/bots#privacy-mode).
 */
@Serializable
public data class ForceReply(
  /**
   * Shows reply interface to the user, as if they manually selected the bot's message and tapped 'Reply'
   */
  public val force_reply: Boolean,
  /**
   * *Optional*. The placeholder to be shown in the input field when the reply is active; 1-64 characters
   */
  public val input_field_placeholder: String? = null,
  /**
   * *Optional*. Use this parameter if you want to force reply from specific users only. Targets: 1) users that are @mentioned in the *text* of the [Message](https://core.telegram.org/bots/api/#message) object; 2) if the bot's message is a reply (has *reply\_to\_message\_id*), sender of the original message.
   */
  public val selective: Boolean? = null,
)
