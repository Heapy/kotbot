package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import kotlin.Long
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Use this method to set a custom title for an administrator in a supergroup promoted by the bot. Returns *True* on success.
 */
@Serializable
public data class SetChatAdministratorCustomTitle(
  /**
   * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
   */
  public val chat_id: ChatId,
  /**
   * Unique identifier of the target user
   */
  public val user_id: Long,
  /**
   * New custom title for the administrator; 0-16 characters, emoji are not allowed
   */
  public val custom_title: String,
)
