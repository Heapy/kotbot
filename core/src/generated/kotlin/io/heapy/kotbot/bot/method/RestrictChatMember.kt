package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.ChatPermissions
import kotlin.Long
import kotlinx.serialization.Serializable

/**
 * Use this method to restrict a user in a supergroup. The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights. Pass *True* for all permissions to lift restrictions from a user. Returns *True* on success.
 */
@Serializable
public data class RestrictChatMember(
  /**
   * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
   */
  public val chat_id: ChatId,
  /**
   * Unique identifier of the target user
   */
  public val user_id: Long,
  /**
   * A JSON-serialized object for new user permissions
   */
  public val permissions: ChatPermissions,
  /**
   * Date when restrictions will be lifted for the user, unix time. If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
   */
  public val until_date: Long? = null,
)
