package io.heapy.kotbot.bot.model

import io.heapy.kotbot.bot.AnyOfArgument
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Represents the [scope](https://core.telegram.org/bots/api/#botcommandscope) of bot commands, covering a specific member of a group or supergroup chat.
 */
@Serializable
public data class BotCommandScopeChatMember(
  /**
   * Scope type, must be *chat\_member*
   */
  public val type: String,
  /**
   * Unique identifier for the target chat or username of the target supergroup (in the format `@supergroupusername`)
   */
  @AnyOfArgument
  public val chat_id: ChatId,
  /**
   * Unique identifier of the target user
   */
  public val user_id: Int,
)
