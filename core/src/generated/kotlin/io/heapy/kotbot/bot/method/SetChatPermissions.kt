package io.heapy.kotbot.bot.method

import io.heapy.kotbot.bot.model.ChatId
import io.heapy.kotbot.bot.model.ChatPermissions
import kotlinx.serialization.Serializable

/**
 * Use this method to set default chat permissions for all members. The bot must be an administrator in the group or a supergroup for this to work and must have the *can_restrict_members* administrator rights. Returns *True* on success.
 */
@Serializable
public data class SetChatPermissions(
  /**
   * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
   */
  public val chat_id: ChatId,
  /**
   * A JSON-serialized object for new default chat permissions
   */
  public val permissions: ChatPermissions,
)
