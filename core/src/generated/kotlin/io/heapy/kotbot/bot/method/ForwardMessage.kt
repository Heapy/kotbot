package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import kotlin.Boolean
import kotlin.Int
import kotlinx.serialization.Serializable

/**
 * Use this method to forward messages of any kind. Service messages can't be forwarded. On success, the sent [Message](https://core.telegram.org/bots/api/#message) is returned.
 */
@Serializable
public data class ForwardMessage(
  /**
   * Unique identifier for the target chat or username of the target channel (in the format `@channelusername`)
   */
  public val chat_id: ChatId,
  /**
   * Unique identifier for the chat where the original message was sent (or channel username in the format `@channelusername`)
   */
  public val from_chat_id: ChatId,
  /**
   * Sends the message [silently](https://telegram.org/blog/channels-2-0#silent-messages). Users will receive a notification with no sound.
   */
  public val disable_notification: Boolean? = null,
  /**
   * Protects the contents of the forwarded message from forwarding and saving
   */
  public val protect_content: Boolean? = null,
  /**
   * Message identifier in the chat specified in *from_chat_id*
   */
  public val message_id: Int,
)
